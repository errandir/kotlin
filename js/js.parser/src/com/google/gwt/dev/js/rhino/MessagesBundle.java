/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gwt.dev.js.rhino;

import java.util.ListResourceBundle;

public class MessagesBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    private static final Object [][] contents = {
        {"msg.java.member.not.found", "Java class \"{0}\" has no public instance field or method named \"{1}\"."},
        {"msg.overlarge.max", "Overly large maximum {0}"},
        {"msg.java.method.assign", "Java method \"{0}\" cannot be assigned to."},
        {"msg.no.paren.catch", "missing ( before catch-block condition"},
        {"msg.constructor.ambiguous", "The choice of Java constructor {0} matching JavaScript argument types ({1}) is ambiguous; candidate constructors are: {2}"},
        {"msg.no.brace.prop", "missing } after property list"},
        {"msg.not.ctor", "\"{0}\" is not a constructor."},
        {"msg.no.paren.after.cond", "missing ) after condition"},
        {"msg.no.paren.with", "missing ( before with-statement object"},
        {"msg.ctor.not.found", "Constructor for \"{0}\" not found."},
        {"msg.no.re.input.for", "no input for {0}"},
        {"msg.java.conversion.implicit_method", "Can''t find converter method \"{0}\" on class {1}."},
        {"msg.continue.outside", "continue must be inside loop."},
        {"msg.no.colon.case", "missing : after case expression"},
        {"msg.default.value", "Cannot find default value for object."},
        {"msg.unterminated.re.lit", "unterminated regular expression literal"},
        {"msg.unterminated.string.lit", "unterminated string literal"},
        {"msg.bad.lhs.assign", "Invalid assignment left-hand side."},
        {"msg.no.paren.cond", "missing ( before condition"},
        {"msg.no.bracket.index", "missing ] in index expression"},
        {"msg.no.function.ref.found", "no source found to decompile function reference {0}"},
        {"msg.caught.nfe", "number format error: {0}"},
        {"msg.jsni.unexpected.identifier", "Unexpected character in JSNI reference"},
        {"msg.bad.switch", "invalid switch statement"},
        {"msg.multiple.ctors", "Cannot have more than one constructor method, but found both {0} and {1}."},
        {"msg.eval.nonstring", "Calling eval() with anything other than a primitive string value will simply return the value. Is this what you intended?"},
        {"msg.no.paren.switch", "missing ( before switch expression"},
        {"msg.obj.getter.parms", "Expected static or delegated getter {0} to take a ScriptableObject parameter."},
        {"msg.unterm.class", "Unterminated character class {0}"},
        {"msg.bad.ctor.sig", "Found constructor with wrong signature: {0} calling {1} with signature {2}"},
        {"msg.bad.radix", "illegal radix {0}."},
        {"msg.invalid.type", "Invalid JavaScript value of type {0}"},
        {"msg.reserved.id", "identifier is a reserved word"},
        {"msg.no.bracket.arg", "missing ] after element list"},
        {"msg.reserved.keyword", "illegal usage of future reserved keyword {0}; interpreting it as ordinary identifier"},
        {"msg.add.sealed", "Cannot add a property to a sealed object."},
        {"msg.extend.scriptable", "{0} must extend ScriptableObject in order to define property {1}."},
        {"msg.token.replaces.pushback", "ungot token {0} replaces pushback token {1}"},
        {"msg.no.paren.for.ctrl", "missing ) after for-loop control"},
        {"msg.bad.label", "invalid label"},
        {"msg.bad.octal.literal", "illegal octal literal digit {0}; interpreting it as a decimal digit"},
        {"msg.no.paren.for", "missing ( after for"},
        {"msg.setter.parms", "Expected either one or two parameters for setter."},
        {"msg.getter.static", "Getter and setter must both be static or neither be static."},
        {"msg.no.regexp", "Regular expressions are not available."},
        {"msg.trail.backslash", "Trailing \\ in regular expression."},
        {"msg.not.class", "Function importClass must be called with a class; had \"{0}\" instead."},
        {"msg.not.java.obj", "Expected argument to getClass() to be a Java object."},
        {"msg.syntax", "syntax error"},
        {"msg.no.colon.cond", "missing : in conditional expression"},
        {"msg.no.while.do", "missing while after do-loop body"},
        {"msg.bad.range", "Invalid range in character class."},
        {"msg.bad.parms", "Bad method parameters for \"{0}\"."},
        {"msg.assn.create", "Assignment to undefined \"{0}\" will create a new variable. Add a variable statement at the top level scope to remove this warning."},
        {"msg.no.brace.body", "missing '{' before function body"},
        {"msg.nonjava.method", "Java method \"{0}\" was invoked with a ''this'' value that was not a Java object."},
        {"msg.bad.precision", "Precision {0} out of range."},
        {"msg.bad.prop", "invalid property id"},
        {"msg.only.from.new", "\"{0}\" may only be invoked from a \"new\" expression."},
        {"msg.try.no.catchfinally", "''try'' without ''catch'' or ''finally''"},
        {"msg.bad.for.in.lhs", "Invalid left-hand side of for..in loop."},
        {"msg.setter2.expected", "Expected static or delegated setter {0} to take two parameters."},
        {"msg.zero.arg.ctor", "Cannot load class \"{0}\" which has no zero-parameter constructor."},
        {"msg.incompat.call", "Method \"{0}\" called on incompatible object."},
        {"msg.missing.exponent", "missing exponent"},
        {"msg.invalid.re.flag", "invalid flag after regular expression"},
        {"msg.no.brace.catchblock", "missing '{' before catch-block body"},
        {"msg.undef.label", "Undefined label {0}."},
        {"msg.fn.redecl", "Function \"{0}\" redeclared; prior definition will be ignored."},
        {"msg.unterminated.comment", "unterminated comment"},
        {"msg.instanceof.bad.prototype", "''prototype'' property of {0} is not an object."},
        {"msg.bad.catchcond", "invalid catch block condition"},
        {"msg.no.paren.after.parms", "missing ) after formal parameters"},
        {"msg.no.paren.parms", "missing ( before function parameters"},
        {"msg.no.colon.prop", "missing : after property id"},
        {"msg.prop.defined", "Cannot import \"{0}\" since a property by that name is already defined."},
        {"msg.is.not.defined", "\"{0}\" is not defined."},
        {"msg.pkg.int", "Java package names may not be numbers."},
        {"msg.cyclic.value", "Cyclic {0} value not allowed."},
        {"msg.no.paren.after.switch", "missing ) after switch expression"},
        {"msg.jsni.unsupported.with", "The ''with'' statement is unsupported in JSNI blocks (perhaps you could use a local variable instead?)"},
        {"msg.bad.var", "missing variable name"},
        {"msg.bad.var.init", "invalid variable initialization"},
        {"msg.ctor.multiple.parms", "Can''t define constructor or class {0} since more than one constructor has multiple parameters."},
        {"msg.bad.break", "Unlabelled break must be inside loop or switch."},
        {"msg.no.java.ctor", "Java constructor for \"{0}\" with arguments \"{1}\" not found."},
        {"msg.max.lt.min", "Maximum {0} less than minimum"},
        {"msg.unterm.quant", "Unterminated quantifier {0}"},
        {"msg.conversion.not.allowed", "Cannot convert {0} to {1}"},
        {"msg.no.overload", "Method \"{0}\" occurs multiple times in class \"{1}\"."},
        {"msg.bad.return", "invalid return"},
        {"msg.no.semi.for.cond", "missing ; after for-loop condition"},
        {"msg.isnt.function", "{0} is not a function."},
        {"msg.no.semi.stmt", "missing ; before statement"},
        {"msg.varargs.ctor", "Method or constructor \"{0}\" must be static with the signature \"(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)\" to define a variable arguments constructor."},
        {"msg.no.paren.arg", "missing ) after argument list"},
        {"msg.cant.instantiate", "error instantiating ({0}): class {1} is interface or abstract"},
        {"msg.deprec.ctor", "The \"{0}\" constructor is deprecated."},
        {"msg.varargs.fun", "Method \"{0}\" must be static with the signature \"(Context cx, Scriptable thisObj, Object[] args, Function funObj)\" to define a variable arguments function."},
        {"msg.dup.label", "Duplicate label {0}."},
        {"msg.unterm.paren", "Unterminated parenthetical {0}"},
        {"msg.oct.esc.too.large", "octal escape too large"},
        {"msg.not.pkg", "Function importPackage must be called with a package; had \"{0}\" instead."},
        {"msg.java.no_such_method", "Can''t find method {0}."},
        {"msg.dup.parms", "Duplicate parameter name \"{0}\"."},
        {"msg.bad.getter.parms", "In order to define a property, getter {0} must have zero parameters or a single ScriptableObject parameter."},
        {"msg.instanceof.not.object", "Can''t use instanceof on a non-object."},
        {"msg.undef.to.object", "Cannot convert undefined to an object."},
        {"msg.null.to.object", "Cannot convert null to an object."},
        {"msg.bad.uri", "Malformed URI sequence."},
        {"msg.method.not.found", "Method \"{0}\" not found in \"{1}\"."},
        {"msg.jsni.expected.param.type", "Expected a valid parameter type signature in JSNI method reference"},
        {"msg.continue.nonloop", "Can only continue to labeled iteration statement."},
        {"msg.no.parm", "missing formal parameter"},
        {"msg.mult.index", "Only one variable allowed in for..in loop."},
        {"msg.no.brace.after.body", "missing } after function body"},
        {"msg.no.paren", "missing ) in parenthetical"},
        {"msg.no.brace.block", "missing } in compound statement"},
        {"msg.no.semi.for", "missing ; after for-loop initializer"},
        {"msg.undefined", "The undefined value has no properties."},
        {"msg.arraylength.bad", "Inappropriate array length."},
        {"msg.prop.not.found", "Property not found."},
        {"msg.zero.quant", "Zero quantifier {0}"},
        {"msg.jsni.expected.char", "Expected \"{0}\" in JSNI reference"},
        {"msg.remove.sealed", "Cannot remove a property from a sealed object."},
        {"msg.no.brace.switch", "missing '{' before switch body"},
        {"msg.bad.default.value", "Object''s getDefaultValue() method returned an object."},
        {"msg.arg.isnt.array", "second argument to Function.prototype.apply must be an array"},
        {"msg.cant.call.indirect", "Function \"{0}\" must be called directly, and not by way of a function of another name."},
        {"msg.cant.convert", "Can''t convert to type \"{0}\"."},
        {"msg.bad.backref", "back-reference exceeds number of capturing parentheses."},
        {"msg.setter2.parms", "Two-parameter setter must take a ScriptableObject as its first parameter."},
        {"msg.primitive.expected", "Primitive type expected (had {0} instead)"},
        {"msg.invalid.escape", "invalid Unicode escape sequence"},
        {"msg.no.paren.after.with", "missing ) after with-statement object"},
        {"msg.illegal.character", "illegal character"},
        {"msg.method.ambiguous", "The choice of Java method {0}.{1} matching JavaScript argument types ({2}) is ambiguous; candidate methods are: {3}"},
        {"msg.bad.esc.mask", "invalid string escape mask"},
        {"msg.catch.unreachable", "any catch clauses following an unqualified catch are unreachable"},
        {"msg.no.name.after.dot", "missing name after . operator"},
        {"msg.java.internal.field.type", "Internal error: type conversion of {0} to assign to {1} on {2} failed."},
        {"msg.no.function.ref.found.in", "no source found in {1} to decompile function reference {0}"},
        {"msg.jsni.expected.identifier", "Expected an identifier in JSNI reference"},
        {"msg.setter1.parms", "Expected single parameter setter for {0}"},
        {"msg.java.internal.private", "Internal error: attempt to access private/protected field \"{0}\"."},
        {"msg.bad.quant", "Invalid quantifier {0}"},
        {"msg.ambig.import", "Ambiguous import: \"{0}\" and and \"{1}\"."},
        {"msg.script.is.not.constructor", "Script objects are not constructors."},
    };
}