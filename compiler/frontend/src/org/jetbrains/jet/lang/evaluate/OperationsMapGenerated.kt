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

package org.jetbrains.jet.lang.evaluate

import java.math.BigInteger
import java.util.HashMap

/** This file is generated by org.jetbrains.jet.generators.evaluate:generate(). DO NOT MODIFY MANUALLY */

private val emptyBinaryFun: Function2<BigInteger, BigInteger, BigInteger> = { a, b -> BigInteger("0") }
private val emptyUnaryFun: Function1<Long, Long> = { a -> 1.toLong() }

private val unaryOperations: HashMap<UnaryOperationKey<*>, Pair<Function1<Any?, Any>, Function1<Long, Long>>>
            = hashMapOf<UnaryOperationKey<*>, Pair<Function1<Any?, Any>, Function1<Long, Long>>>(
    unaryOperation(BOOLEAN, "not", { a -> a.not() }, emptyUnaryFun),
    unaryOperation(BOOLEAN, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(BYTE, "minus", { a -> a.minus() }, { a -> a.minus() }),
    unaryOperation(BYTE, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(BYTE, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(BYTE, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(BYTE, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(BYTE, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(BYTE, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(BYTE, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(BYTE, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(BYTE, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(CHAR, "minus", { a -> a.minus() }, emptyUnaryFun),
    unaryOperation(CHAR, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(CHAR, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(CHAR, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(CHAR, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(CHAR, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(CHAR, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(CHAR, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(CHAR, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(CHAR, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "minus", { a -> a.minus() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(DOUBLE, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(FLOAT, "minus", { a -> a.minus() }, emptyUnaryFun),
    unaryOperation(FLOAT, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(FLOAT, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(INT, "inv", { a -> a.inv() }, emptyUnaryFun),
    unaryOperation(INT, "minus", { a -> a.minus() }, { a -> a.minus() }),
    unaryOperation(INT, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(INT, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(INT, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(INT, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(INT, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(INT, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(INT, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(INT, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(INT, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(LONG, "inv", { a -> a.inv() }, emptyUnaryFun),
    unaryOperation(LONG, "minus", { a -> a.minus() }, { a -> a.minus() }),
    unaryOperation(LONG, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(LONG, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(LONG, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(LONG, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(LONG, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(LONG, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(LONG, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(LONG, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(LONG, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(SHORT, "minus", { a -> a.minus() }, { a -> a.minus() }),
    unaryOperation(SHORT, "plus", { a -> a.plus() }, emptyUnaryFun),
    unaryOperation(SHORT, "toByte", { a -> a.toByte() }, emptyUnaryFun),
    unaryOperation(SHORT, "toChar", { a -> a.toChar() }, emptyUnaryFun),
    unaryOperation(SHORT, "toDouble", { a -> a.toDouble() }, emptyUnaryFun),
    unaryOperation(SHORT, "toFloat", { a -> a.toFloat() }, emptyUnaryFun),
    unaryOperation(SHORT, "toInt", { a -> a.toInt() }, emptyUnaryFun),
    unaryOperation(SHORT, "toLong", { a -> a.toLong() }, emptyUnaryFun),
    unaryOperation(SHORT, "toShort", { a -> a.toShort() }, emptyUnaryFun),
    unaryOperation(SHORT, "toString", { a -> a.toString() }, emptyUnaryFun),
    unaryOperation(STRING, "toString", { a -> a.toString() }, emptyUnaryFun)
)

private val binaryOperations: HashMap<BinaryOperationKey<*, *>, Pair<Function2<Any?, Any?, Any>, Function2<BigInteger, BigInteger, BigInteger>>>
            = hashMapOf<BinaryOperationKey<*, *>, Pair<Function2<Any?, Any?, Any>, Function2<BigInteger, BigInteger, BigInteger>>>(
    binaryOperation(BOOLEAN, BOOLEAN, "and", { a, b -> a.and(b) }, emptyBinaryFun),
    binaryOperation(BOOLEAN, BOOLEAN, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BOOLEAN, BOOLEAN, "or", { a, b -> a.or(b) }, emptyBinaryFun),
    binaryOperation(BOOLEAN, BOOLEAN, "xor", { a, b -> a.xor(b) }, emptyBinaryFun),
    binaryOperation(BOOLEAN, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(BYTE, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(BYTE, BYTE, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(BYTE, CHAR, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(BYTE, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(BYTE, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(BYTE, INT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(BYTE, LONG, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(BYTE, SHORT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(BYTE, BYTE, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(BYTE, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(BYTE, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(BYTE, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(BYTE, INT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(BYTE, LONG, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(BYTE, SHORT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(BYTE, BYTE, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(BYTE, CHAR, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(BYTE, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(BYTE, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(BYTE, INT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(BYTE, LONG, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(BYTE, SHORT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(BYTE, BYTE, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(BYTE, CHAR, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(BYTE, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(BYTE, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(BYTE, INT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(BYTE, LONG, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(BYTE, SHORT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(BYTE, BYTE, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(BYTE, CHAR, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(BYTE, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(BYTE, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(BYTE, INT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(BYTE, LONG, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(BYTE, SHORT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(BYTE, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(CHAR, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(CHAR, BYTE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(CHAR, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(CHAR, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(CHAR, INT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(CHAR, LONG, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(CHAR, SHORT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(CHAR, BYTE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, INT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, LONG, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, SHORT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, BYTE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(CHAR, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(CHAR, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(CHAR, INT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(CHAR, LONG, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(CHAR, SHORT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(CHAR, BYTE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, INT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, LONG, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, SHORT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(CHAR, BYTE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(CHAR, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(CHAR, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(CHAR, INT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(CHAR, LONG, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(CHAR, SHORT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(CHAR, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, BYTE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, CHAR, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, INT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, LONG, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, SHORT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, BYTE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, INT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, LONG, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, SHORT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, BYTE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, INT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, LONG, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, SHORT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, BYTE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, CHAR, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, INT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, LONG, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, SHORT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, BYTE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, CHAR, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, INT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, LONG, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, SHORT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(DOUBLE, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, BYTE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, CHAR, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, INT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, LONG, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, SHORT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, BYTE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, INT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, LONG, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, SHORT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, BYTE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, CHAR, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, INT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, LONG, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, SHORT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, BYTE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, CHAR, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, INT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, LONG, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, SHORT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, BYTE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, CHAR, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, INT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, LONG, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, SHORT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(FLOAT, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "and", { a, b -> a.and(b) }, { a, b -> a.and(b) }),
    binaryOperation(INT, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(INT, BYTE, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(INT, CHAR, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(INT, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(INT, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(INT, LONG, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(INT, SHORT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(INT, BYTE, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(INT, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(INT, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(INT, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(INT, LONG, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(INT, SHORT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(INT, BYTE, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(INT, CHAR, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(INT, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(INT, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(INT, LONG, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(INT, SHORT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(INT, INT, "or", { a, b -> a.or(b) }, { a, b -> a.or(b) }),
    binaryOperation(INT, BYTE, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(INT, CHAR, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(INT, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(INT, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(INT, LONG, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(INT, SHORT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(INT, INT, "shl", { a, b -> a.shl(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "shr", { a, b -> a.shr(b) }, emptyBinaryFun),
    binaryOperation(INT, BYTE, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(INT, CHAR, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(INT, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(INT, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(INT, LONG, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(INT, SHORT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(INT, INT, "ushr", { a, b -> a.ushr(b) }, emptyBinaryFun),
    binaryOperation(INT, INT, "xor", { a, b -> a.xor(b) }, { a, b -> a.xor(b) }),
    binaryOperation(INT, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(LONG, LONG, "and", { a, b -> a.and(b) }, { a, b -> a.and(b) }),
    binaryOperation(LONG, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(LONG, BYTE, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(LONG, CHAR, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(LONG, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(LONG, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(LONG, LONG, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(LONG, SHORT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(LONG, BYTE, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(LONG, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(LONG, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(LONG, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(LONG, LONG, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(LONG, SHORT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(LONG, BYTE, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(LONG, CHAR, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(LONG, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(LONG, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(LONG, LONG, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(LONG, SHORT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(LONG, LONG, "or", { a, b -> a.or(b) }, { a, b -> a.or(b) }),
    binaryOperation(LONG, BYTE, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(LONG, CHAR, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(LONG, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(LONG, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(LONG, LONG, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(LONG, SHORT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(LONG, INT, "shl", { a, b -> a.shl(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "shr", { a, b -> a.shr(b) }, emptyBinaryFun),
    binaryOperation(LONG, BYTE, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(LONG, CHAR, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(LONG, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(LONG, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(LONG, INT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(LONG, LONG, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(LONG, SHORT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(LONG, INT, "ushr", { a, b -> a.ushr(b) }, emptyBinaryFun),
    binaryOperation(LONG, LONG, "xor", { a, b -> a.xor(b) }, { a, b -> a.xor(b) }),
    binaryOperation(LONG, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(SHORT, BYTE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, CHAR, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, DOUBLE, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, FLOAT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, INT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, LONG, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, SHORT, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(SHORT, BYTE, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(SHORT, CHAR, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(SHORT, DOUBLE, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(SHORT, FLOAT, "div", { a, b -> a.div(b) }, emptyBinaryFun),
    binaryOperation(SHORT, INT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(SHORT, LONG, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(SHORT, SHORT, "div", { a, b -> a.div(b) }, { a, b -> a.divide(b) }),
    binaryOperation(SHORT, BYTE, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(SHORT, CHAR, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(SHORT, DOUBLE, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(SHORT, FLOAT, "minus", { a, b -> a.minus(b) }, emptyBinaryFun),
    binaryOperation(SHORT, INT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(SHORT, LONG, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(SHORT, SHORT, "minus", { a, b -> a.minus(b) }, { a, b -> a.subtract(b) }),
    binaryOperation(SHORT, BYTE, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(SHORT, CHAR, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(SHORT, DOUBLE, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(SHORT, FLOAT, "mod", { a, b -> a.mod(b) }, emptyBinaryFun),
    binaryOperation(SHORT, INT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(SHORT, LONG, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(SHORT, SHORT, "mod", { a, b -> a.mod(b) }, { a, b -> a.mod(b) }),
    binaryOperation(SHORT, BYTE, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(SHORT, CHAR, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(SHORT, DOUBLE, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(SHORT, FLOAT, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(SHORT, INT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(SHORT, LONG, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(SHORT, SHORT, "plus", { a, b -> a.plus(b) }, { a, b -> a.add(b) }),
    binaryOperation(SHORT, BYTE, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(SHORT, CHAR, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(SHORT, DOUBLE, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(SHORT, FLOAT, "times", { a, b -> a.times(b) }, emptyBinaryFun),
    binaryOperation(SHORT, INT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(SHORT, LONG, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(SHORT, SHORT, "times", { a, b -> a.times(b) }, { a, b -> a.multiply(b) }),
    binaryOperation(SHORT, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun),
    binaryOperation(STRING, STRING, "compareTo", { a, b -> a.compareTo(b) }, emptyBinaryFun),
    binaryOperation(STRING, INT, "get", { a, b -> a.get(b) }, emptyBinaryFun),
    binaryOperation(STRING, ANY, "plus", { a, b -> a.plus(b) }, emptyBinaryFun),
    binaryOperation(STRING, ANY, "equals", { a, b -> a.equals(b) }, emptyBinaryFun)
)
