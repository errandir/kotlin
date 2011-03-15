package org.jetbrains.jet.lang.types;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.JetNodeTypes;
import org.jetbrains.jet.lang.JetSemanticServices;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.jet.lang.resolve.*;
import org.jetbrains.jet.lexer.JetTokens;

import java.util.*;

/**
 * @author abreslav
 */
public class JetTypeInferrer {

    private final BindingTrace trace;
    private final JetSemanticServices semanticServices;
    private final TypeResolver typeResolver;
    private final ClassDescriptorResolver classDescriptorResolver;

    public JetTypeInferrer(BindingTrace trace, JetSemanticServices semanticServices) {
        this.trace = trace;
        this.semanticServices = semanticServices;
        this.typeResolver = new TypeResolver(trace, semanticServices);
        this.classDescriptorResolver = new ClassDescriptorResolver(semanticServices, trace);
    }

    @NotNull
    public JetType safeGetType(@NotNull final JetScope scope, @NotNull JetExpression expression, final boolean preferBlock) {
        JetType type = getType(scope, expression, preferBlock);
        if (type != null) {
            return type;
        }
        return ErrorType.createErrorType("Type for " + expression.getText());
    }

    @Nullable
    public JetType getType(@NotNull final JetScope scope, @NotNull JetExpression expression, final boolean preferBlock) {
        final JetType[] result = new JetType[1];
        expression.accept(new JetVisitor() {
            @Override
            public void visitReferenceExpression(JetSimpleNameExpression expression) {
                // TODO : other members
                // TODO : type substitutions???
                String referencedName = expression.getReferencedName();
                PropertyDescriptor property = scope.getProperty(referencedName);
                if (property != null) {
                    trace.recordReferenceResolution(expression, property);
                    result[0] = property.getType();
                    return;
                } else {
                    NamespaceDescriptor namespace = scope.getNamespace(referencedName);
                    if (namespace != null) {
                        trace.recordReferenceResolution(expression, namespace);
                        result[0] = namespace.getNamespaceType();
                        return;
                    }
                }
                semanticServices.getErrorHandler().unresolvedReference(expression);
            }

            @Override
            public void visitFunctionLiteralExpression(JetFunctionLiteralExpression expression) {
                if (preferBlock && !expression.hasParameterSpecification()) {
                    result[0] = getBlockReturnedType(scope, expression.getBody());
                    return;
                }

                FunctionDescriptorImpl functionDescriptor = new FunctionDescriptorImpl(scope.getContainingDeclaration(), Collections.<Attribute>emptyList(), "<anonymous>");

                JetTypeReference returnTypeRef = expression.getReturnTypeRef();

                JetTypeReference receiverTypeRef = expression.getReceiverTypeRef();
                final JetType receiverType;
                if (receiverTypeRef != null) {
                    receiverType = typeResolver.resolveType(scope, receiverTypeRef);
                } else {
                    receiverType = scope.getThisType();
                }

                List<JetElement> body = expression.getBody();
                final Map<String, PropertyDescriptor> parameterDescriptors = new HashMap<String, PropertyDescriptor>();
                List<JetType> parameterTypes = new ArrayList<JetType>();
                for (JetParameter parameter : expression.getParameters()) {
                    JetTypeReference typeReference = parameter.getTypeReference();
                    if (typeReference == null) {
                        throw new UnsupportedOperationException("Type inference for parameters is not implemented yet");
                    }
                    PropertyDescriptor propertyDescriptor = classDescriptorResolver.resolvePropertyDescriptor(functionDescriptor, scope, parameter);
                    parameterDescriptors.put(parameter.getName(), propertyDescriptor);
                    parameterTypes.add(propertyDescriptor.getType());
                }
                JetType returnType;
                if (returnTypeRef != null) {
                    returnType = typeResolver.resolveType(scope, returnTypeRef);
                } else {
                    WritableScope writableScope = new WritableScope(scope, functionDescriptor);
                    for (PropertyDescriptor propertyDescriptor : parameterDescriptors.values()) {
                        writableScope.addPropertyDescriptor(propertyDescriptor);
                    }
                    writableScope.setThisType(receiverType);
                    returnType = getBlockReturnedType(writableScope, body);
                }
                result[0] = JetStandardClasses.getFunctionType(null, receiverTypeRef == null ? null : receiverType, parameterTypes, returnType);
            }

            @Override
            public void visitParenthesizedExpression(JetParenthesizedExpression expression) {
                result[0] = getType(scope, expression.getExpression(), false);
            }

            @Override
            public void visitConstantExpression(JetConstantExpression expression) {
                IElementType elementType = expression.getNode().getElementType();
                JetStandardLibrary standardLibrary = semanticServices.getStandardLibrary();
                if (elementType == JetNodeTypes.INTEGER_CONSTANT) {
                    result[0] = standardLibrary.getIntType();
                } else if (elementType == JetNodeTypes.LONG_CONSTANT) {
                    result[0] = standardLibrary.getLongType();
                } else if (elementType == JetNodeTypes.FLOAT_CONSTANT) {
                    String text = expression.getText();
                    assert text.length() > 0;
                    char lastChar = text.charAt(text.length() - 1);
                    if (lastChar == 'f' || lastChar == 'F') {
                        result[0] = standardLibrary.getFloatType();
                    } else {
                        result[0] = standardLibrary.getDoubleType();
                    }
                } else if (elementType == JetNodeTypes.BOOLEAN_CONSTANT) {
                    result[0] = standardLibrary.getBooleanType();
                } else if (elementType == JetNodeTypes.CHARACTER_CONSTANT) {
                    result[0] = standardLibrary.getCharType();
                } else if (elementType == JetNodeTypes.STRING_CONSTANT) {
                    result[0] = standardLibrary.getStringType();
                } else if (elementType == JetNodeTypes.NULL) {
                    result[0] = JetStandardClasses.getNullableNothingType();
                } else {
                    throw new IllegalArgumentException("Unsupported constant: " + expression);
                }
            }

            @Override
            public void visitThrowExpression(JetThrowExpression expression) {
                result[0] = JetStandardClasses.getNothingType();
            }

            @Override
            public void visitReturnExpression(JetReturnExpression expression) {
                JetExpression returnedExpression = expression.getReturnedExpression();
                if (returnedExpression != null) {
                    getType(scope, returnedExpression, false);
                }
                result[0] = JetStandardClasses.getNothingType();
            }

            @Override
            public void visitBreakExpression(JetBreakExpression expression) {
                result[0] = JetStandardClasses.getNothingType();
            }

            @Override
            public void visitContinueExpression(JetContinueExpression expression) {
                result[0] = JetStandardClasses.getNothingType();
            }

            @Override
            public void visitTypeofExpression(JetTypeofExpression expression) {
                throw new UnsupportedOperationException("Return some reflection interface"); // TODO
            }

            @Override
            public void visitBinaryWithTypeRHSExpression(JetBinaryExpressionWithTypeRHS expression) {
                if (expression.getOperationReference().getReferencedNameElementType() == JetTokens.COLON) {
                    JetType actualType = getType(scope, expression.getLeft(), false);
                    JetType expectedType = typeResolver.resolveType(scope, expression.getRight());
                    if (actualType != null && !semanticServices.getTypeChecker().isSubtypeOf(actualType, expectedType)) {
                        // TODO
                        semanticServices.getErrorHandler().typeMismatch(expression.getLeft(), expectedType, actualType);
                    }
                    result[0] = expectedType;
                    return;
                }
                throw new UnsupportedOperationException(); // TODO
            }

            @Override
            public void visitIfExpression(JetIfExpression expression) {
                // TODO : check condition type
                // TODO : change types according to is and nullability checks
                JetExpression elseBranch = expression.getElse();
                if (elseBranch == null) {
                    // TODO : type-check the branch
                    result[0] = JetStandardClasses.getUnitType();
                } else {
                    JetType thenType = getType(scope, expression.getThen(), true);
                    JetType elseType = getType(scope, elseBranch, true);
                    result[0] = semanticServices.getTypeChecker().commonSupertype(Arrays.asList(thenType, elseType));
                }
            }

            @Override
            public void visitWhenExpression(JetWhenExpression expression) {
                // TODO :change scope according to the bound value in the when header
                List<JetType> expressions = new ArrayList<JetType>();
                collectAllReturnTypes(expression, scope, expressions);
                result[0] = semanticServices.getTypeChecker().commonSupertype(expressions);
            }

            @Override
            public void visitTryExpression(JetTryExpression expression) {
                JetExpression tryBlock = expression.getTryBlock();
                List<JetCatchClause> catchClauses = expression.getCatchClauses();
                JetFinallySection finallyBlock = expression.getFinallyBlock();
                List<JetType> types = new ArrayList<JetType>();
                if (finallyBlock == null) {
                    for (JetCatchClause catchClause : catchClauses) {
                        // TODO: change scope here
                        types.add(getType(scope, catchClause.getCatchBody(), true));
                    }
                } else {
                    types.add(getType(scope, finallyBlock.getFinalExpression(), true));
                }
                types.add(getType(scope, tryBlock, true));
                result[0] = semanticServices.getTypeChecker().commonSupertype(types);
            }

            @Override
            public void visitTupleExpression(JetTupleExpression expression) {
                List<JetExpression> entries = expression.getEntries();
                List<JetType> types = new ArrayList<JetType>();
                for (JetExpression entry : entries) {
                    types.add(getType(scope, entry, false));
                }
                // TODO : labels
                result[0] = JetStandardClasses.getTupleType(types);
            }

            @Override
            public void visitThisExpression(JetThisExpression expression) {
                // TODO : qualified this, e.g. Foo.this<Bar>
                JetType thisType = scope.getThisType();
                JetTypeReference superTypeQualifier = expression.getSuperTypeQualifier();
                if (superTypeQualifier != null) {
                    // This cast must be safe (assuming the PSI doesn't contain errors)
                    JetUserType typeElement = (JetUserType) superTypeQualifier.getTypeElement();
                    ClassDescriptor superclass = typeResolver.resolveClass(scope, typeElement);
                    Collection<? extends JetType> supertypes = thisType.getConstructor().getSupertypes();
                    Map<TypeConstructor, TypeProjection> substitutionContext = TypeSubstitutor.INSTANCE.getSubstitutionContext(thisType);
                    for (JetType declaredSupertype : supertypes) {
                        if (declaredSupertype.getConstructor().equals(superclass.getTypeConstructor())) {
                            result[0] = TypeSubstitutor.INSTANCE.substitute(substitutionContext, declaredSupertype, Variance.INVARIANT);
                            break;
                        }
                    }
                    assert result[0] != null;
                } else {
                    result[0] = thisType;
                }
            }

            @Override
            public void visitBlockExpression(JetBlockExpression expression) {
                result[0] = getBlockReturnedType(scope, expression.getStatements());
            }

            @Override
            public void visitLoopExpression(JetLoopExpression expression) {
                result[0] = JetStandardClasses.getUnitType();
            }

            @Override
            public void visitNewExpression(JetNewExpression expression) {
                // TODO : type argument inference
                JetTypeReference typeReference = expression.getTypeReference();
                result[0] = typeResolver.resolveType(scope, typeReference);
            }

            @Override
            public void visitDotQualifiedExpression(JetDotQualifiedExpression expression) {
                // TODO : functions
                JetExpression receiverExpression = expression.getReceiverExpression();
                JetExpression selectorExpression = expression.getSelectorExpression();
                JetType receiverType = getType(scope, receiverExpression, false);
                if (receiverType != null) { // TODO : review
                    JetScope compositeScope = new ScopeWithReceiver(scope, receiverType);
                    result[0] = getType(compositeScope, selectorExpression, false);
                }
            }

            @Override
            public void visitCallExpression(JetCallExpression expression) {
                JetExpression calleeExpression = expression.getCalleeExpression();

                // 1) ends with a name -> (scope, name) to look up
                // 2) ends with something else -> just check types

                // TODO : check somewhere that these are NOT projections
                List<JetTypeProjection> typeArguments = expression.getTypeArguments();

                List<JetArgument> valueArguments = expression.getValueArguments();

                boolean someNamed = false;
                for (JetArgument argument : valueArguments) {
                    if (argument.isNamed()) {
                        someNamed = true;
                        break;
                    }
                }
                List<JetExpression> functionLiteralArguments = expression.getFunctionLiteralArguments();

//                JetExpression functionLiteralArgument = functionLiteralArguments.isEmpty() ? null : functionLiteralArguments.get(0);
                // TODO : must be a check
                assert functionLiteralArguments.size() <= 1;

                OverloadDomain overloadDomain = getOverloadDomain(scope, calleeExpression);
                if (someNamed) {
                    // TODO : check that all are named
                    throw new UnsupportedOperationException(); // TODO

//                    result[0] = overloadDomain.getFunctionDescriptorForNamedArguments(typeArguments, valueArguments, functionLiteralArgument);
                } else {
                    List<JetType> types = new ArrayList<JetType>();
                    for (JetTypeProjection projection : typeArguments) {
                        // TODO : check that there's no projection
                        types.add(typeResolver.resolveType(scope, projection.getTypeReference()));
                    }

                    List<JetExpression> positionedValueArguments = new ArrayList<JetExpression>();
                    for (JetArgument argument : valueArguments) {
                        positionedValueArguments.add(argument.getArgumentExpression());
                    }

                    positionedValueArguments.addAll(functionLiteralArguments);

                    List<JetType> valueArgumentTypes = new ArrayList<JetType>();
                    for (JetExpression valueArgument : positionedValueArguments) {
                        valueArgumentTypes.add(getType(scope, valueArgument, false));
                    }

                    FunctionDescriptor functionDescriptor = overloadDomain.getFunctionDescriptorForPositionedArguments(types, valueArgumentTypes);
                    if (functionDescriptor != null) {
                        result[0] = functionDescriptor.getUnsubstitutedReturnType();
                    }
                }
            }

            @Override
            public void visitBinaryExpression(JetBinaryExpression expression) {
                JetSimpleNameExpression operationSign = expression.getOperationReference();

                IElementType operationType = operationSign.getReferencedNameElementType();
                if (operationType == JetTokens.IDENTIFIER) {
                    result[0] = getTypeForBinaryCall(expression, operationSign.getReferencedName(), scope);
                }
                else if (operationType == JetTokens.PLUS) {
                    result[0] = getTypeForBinaryCall(expression, "plus", scope);
                }
                else if (operationType == JetTokens.EQ) {
                    JetExpression left = expression.getLeft();
                    JetExpression deparenthesized = deparenthesize(left);
                    if (deparenthesized instanceof JetArrayAccessExpression) {
                        JetArrayAccessExpression arrayAccessExpression = (JetArrayAccessExpression) deparenthesized;
                        resolveArrayAccessToLValue(arrayAccessExpression, expression.getRight(), expression.getOperationReference());
                    }
                    else {
                        getType(scope, expression.getRight(), false);
                        //throw new UnsupportedOperationException();
                    }
                    result[0] = null; // TODO : This is not an expression, in fact!
                } else {
                    throw new UnsupportedOperationException(); // TODO
                }
            }

            @Override
            public void visitArrayAccessExpression(JetArrayAccessExpression expression) {
                JetExpression arrayExpression = expression.getArrayExpression();
                JetType receiverType = getType(scope, arrayExpression, false);
                List<JetExpression> indexExpressions = expression.getIndexExpressions();
                List<JetType> argumentTypes = getTypes(scope, indexExpressions);
                if (argumentTypes == null) return;

                FunctionDescriptor functionDescriptor = lookupFunction(scope, expression, "get", receiverType, argumentTypes);
                if (functionDescriptor != null) {
                    result[0] = functionDescriptor.getUnsubstitutedReturnType();
                }
            }

            private void resolveArrayAccessToLValue(JetArrayAccessExpression arrayAccessExpression, JetExpression rightHandSide, JetSimpleNameExpression operationSign) {
                List<JetType> argumentTypes = getTypes(scope, arrayAccessExpression.getIndexExpressions());
                if (argumentTypes == null) return;
                JetType rhsType = getType(scope, rightHandSide, false);
                if (rhsType == null) return;
                argumentTypes.add(rhsType);

                JetType receiverType = getType(scope, arrayAccessExpression.getArrayExpression(), false);
                if (receiverType == null) return;

                // TODO : nasty hack: effort is duplicated
                lookupFunction(scope, arrayAccessExpression, "set", receiverType, argumentTypes);
                FunctionDescriptor functionDescriptor = lookupFunction(scope, operationSign, "set", receiverType, argumentTypes);
                if (functionDescriptor != null) {
                    result[0] = functionDescriptor.getUnsubstitutedReturnType();
                }
            }

            @Override
            public void visitJetElement(JetElement elem) {
                throw new IllegalArgumentException("Unsupported element: " + elem);
            }

            private JetType getTypeForBinaryCall(JetBinaryExpression expression, String name, JetScope scope) {
                JetSimpleNameExpression operationSign = expression.getOperationReference();
                JetExpression left = expression.getLeft();
                JetType leftType = getType(scope, left, false);
                JetExpression right = expression.getRight();
                if (right == null) {
                    return ErrorType.createErrorType("No right argument");
                }
                JetType rightType = getType(scope, right, false);
                FunctionDescriptor functionDescriptor = lookupFunction(scope, operationSign, name, leftType, Collections.singletonList(rightType));
                if (functionDescriptor != null) {
                    return functionDescriptor.getUnsubstitutedReturnType();
                }
                return null;
            }
        });
        if (result[0] != null) {
            trace.recordExpressionType(expression, result[0]);
        }
        return result[0];
    }

    @NotNull
    private JetExpression deparenthesize(@NotNull JetExpression expression) {
        while (expression instanceof JetParenthesizedExpression) {
            expression = ((JetParenthesizedExpression) expression).getExpression();
        }
        return expression;
    }

    @Nullable
    private FunctionDescriptor lookupFunction(JetScope scope, JetReferenceExpression reference, String name, JetType receiverType, List<JetType> argumentTypes) {
        OverloadDomain overloadDomain = semanticServices.getOverloadResolver().getOverloadDomain(receiverType, scope, name);
        overloadDomain = wrapForTracing(overloadDomain, reference);
        return overloadDomain.getFunctionDescriptorForPositionedArguments(Collections.<JetType>emptyList(), argumentTypes);
    }

    @Nullable
    private List<JetType> getTypes(JetScope scope, List<JetExpression> indexExpressions) {
        List<JetType> argumentTypes = new ArrayList<JetType>();
        for (JetExpression indexExpression : indexExpressions) {
            JetType type = getType(scope, indexExpression, false);
            if (type == null) {
                return null;
            }
            argumentTypes.add(type);
        }
        return argumentTypes;
    }


    private OverloadDomain getOverloadDomain(final JetScope scope, JetExpression calleeExpression) {
        final OverloadDomain[] result = new OverloadDomain[1];
        final JetSimpleNameExpression[] reference = new JetSimpleNameExpression[1];
        calleeExpression.accept(new JetVisitor() {

            @Override
            public void visitHashQualifiedExpression(JetHashQualifiedExpression expression) {
                // a#b -- create a domain for all overloads of b in a
                throw new UnsupportedOperationException(); // TODO
            }

            @Override
            public void visitPredicateExpression(JetPredicateExpression expression) {
                // overload lookup for checking, but the type is receiver's type + nullable
                throw new UnsupportedOperationException(); // TODO
            }

            @Override
            public void visitQualifiedExpression(JetQualifiedExpression expression) {
                // . or ?.
                JetExpression selectorExpression = expression.getSelectorExpression();
                if (selectorExpression instanceof JetSimpleNameExpression) {
                    JetSimpleNameExpression referenceExpression = (JetSimpleNameExpression) selectorExpression;

                    JetType receiverType = getType(scope, expression.getReceiverExpression(), false);
                    if (receiverType != null) {
                        result[0] = semanticServices.getOverloadResolver().getOverloadDomain(receiverType, scope, referenceExpression.getReferencedName());
                        reference[0] = referenceExpression;
                    }
                } else {
                    throw new UnsupportedOperationException(); // TODO
                }
            }

            @Override
            public void visitReferenceExpression(JetSimpleNameExpression expression) {
                // a -- create a hierarchical lookup domain for this.a
                result[0] = semanticServices.getOverloadResolver().getOverloadDomain(null, scope, expression.getReferencedName());
                reference[0] = expression;
            }

            @Override
            public void visitExpression(JetExpression expression) {
                // <e> create a dummy domain for the type of e
                throw new UnsupportedOperationException(); // TODO
            }

            @Override
            public void visitJetElement(JetElement elem) {
                throw new IllegalArgumentException("Unsupported element: " + elem);
            }
        });
        return wrapForTracing(result[0], reference[0]);
    }

    private OverloadDomain wrapForTracing(final OverloadDomain overloadDomain, @NotNull final JetReferenceExpression referenceExpression) {
        if (overloadDomain == null) return OverloadDomain.EMPTY;
        return new OverloadDomain() {
            @Override
            public FunctionDescriptor getFunctionDescriptorForNamedArguments(@NotNull List<JetType> typeArguments, @NotNull Map<String, JetType> valueArgumentTypes, @Nullable JetType functionLiteralArgumentType) {
                FunctionDescriptor descriptor = overloadDomain.getFunctionDescriptorForNamedArguments(typeArguments, valueArgumentTypes, functionLiteralArgumentType);
                if (descriptor != null) {
                    trace.recordReferenceResolution(referenceExpression, descriptor);
                } else {
                    semanticServices.getErrorHandler().unresolvedReference(referenceExpression);
                }
                return descriptor;
            }

            @Override
            public FunctionDescriptor getFunctionDescriptorForPositionedArguments(@NotNull List<JetType> typeArguments, @NotNull List<JetType> positionedValueArgumentTypes) {
                FunctionDescriptor descriptor = overloadDomain.getFunctionDescriptorForPositionedArguments(typeArguments, positionedValueArgumentTypes);
                if (descriptor != null) {
                    trace.recordReferenceResolution(referenceExpression, descriptor);
                } else {
                    semanticServices.getErrorHandler().unresolvedReference(referenceExpression);
                }
                return descriptor;
            }
        };
    }

    private JetType getBlockReturnedType(@NotNull JetScope outerScope, List<JetElement> block) {
        if (block.isEmpty()) {
            return JetStandardClasses.getUnitType();
        } else {
            DeclarationDescriptor containingDescriptor = outerScope.getContainingDeclaration();
            WritableScope scope = new WritableScope(outerScope, containingDescriptor);
            for (JetElement statement : block) {
                // TODO: consider other declarations
                if (statement instanceof JetProperty) {
                    JetProperty property = (JetProperty) statement;
                    PropertyDescriptor propertyDescriptor = classDescriptorResolver.resolvePropertyDescriptor(containingDescriptor, scope, property);
                    scope.addPropertyDescriptor(propertyDescriptor);
                    trace.recordDeclarationResolution(property, propertyDescriptor);
                }
                else if (statement instanceof JetExpression) {
                    getType(scope, (JetExpression) statement, true);
                }
                else {
                    throw new UnsupportedOperationException(); // TODO
                }
            }
            JetElement lastElement = block.get(block.size() - 1);
            if (lastElement instanceof JetExpression) {
                JetExpression expression = (JetExpression) lastElement;
                return getType(scope, expression, true);
            }
            // TODO: functions, classes, etc.
            throw new IllegalArgumentException("Last item in the block must be an expression, but was " + lastElement.getClass().getCanonicalName());
        }
    }

    private void collectAllReturnTypes(JetWhenExpression whenExpression, JetScope scope, List<JetType> result) {
        for (JetWhenEntry entry : whenExpression.getEntries()) {
            JetWhenExpression subWhen = entry.getSubWhen();
            if (subWhen != null) {
                collectAllReturnTypes(subWhen, scope, result);
            } else {
                JetExpression resultExpression = entry.getExpression();
                if (resultExpression != null) {
                    result.add(getType(scope, resultExpression, true));
                }
            }
        }
    }

}
