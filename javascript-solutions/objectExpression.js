"use strict";

function Expression(operator, diffOperator, string, args) {
    this.diff = k => diffOperator(...args, ...args.map(x => x.diff(k)));
    this.toString = () => args.concat(string).join(" ");
    this.evaluate = (...evalArgs) => operator(...args.map(a => a.evaluate(...evalArgs)));
    this.prefix = () => `(${string} ${args.map(x => x.prefix()).join(" ")})`;
    this.postfix = () => `(${args.map(x => x.postfix()).join(" ")} ${string})`;
}

function Atom(field) {
    this.toString = () => field.toString();
    this.evaluate = (...args) => isInt(field) ? field : args[field.charCodeAt(0) - 120];
    this.diff = (k) => field === k ? Const.ONE : Const.ZERO;
    this.prefix = this.toString;
    this.postfix = this.toString;
}

function Const(value) {
    Atom.call(this, value);
}
Const.prototype = Object.create(Atom);
Const.prototype.constructor = Const;
Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);
Const.E = new Const(Math.E);

const strX = "x";
const strY = "y";
const strZ = "z";
function Variable(name) {
    Atom.call(this, name);
}
Variable.prototype = Object.create(Atom);
Variable.prototype.constructor = Variable;
Variable.X = new Variable(strX);
Variable.Y = new Variable(strY);
Variable.Z = new Variable(strZ);

function generateExpression(operator, diffOperator, string) {
    const fun = function(...cArgs) {
        Expression.call(this, operator, diffOperator, string, cArgs)
    }
    fun.prototype = Object.create(Expression);
    fun.prototype.constructor = fun;
    return fun;
}

const strNegate = "negate";
let Negate = generateExpression(
    x => -x,
    (x, dx) => new Negate(dx),
    strNegate,
);

const strAdd = "+";
let Add = generateExpression(
    (x, y) => x + y,
    (x, y, dx, dy) => new Add(dx, dy),
    strAdd,
);

const strSubtract = "-";
let Subtract = generateExpression(
    (x, y) => x - y,
    (x, y, dx, dy) => new Subtract(dx, dy),
    strSubtract,
);

const strMultiply = "*";
let Multiply = generateExpression(
    (x, y) => x * y,
    (x, y, dx, dy) => new Add(new Multiply(x, dy), new Multiply(y, dx)),
    strMultiply,
);

const strDivide = "/";
let Divide = generateExpression(
    (x, y) => x / y,
    (x, y, dx, dy) =>
        new Divide(
            new Subtract(new Multiply(dx, y), new Multiply(x, dy)),
            new Multiply(y, y),
        ),
    strDivide,
);

const strPow = "pow";
let Pow = generateExpression(
    Math.pow,
    (x, y, dx, dy) =>
        new Multiply(
            new Pow(x, y),
            new Add(
                new Multiply(new Log(Const.E, x), dy),
                new Divide(new Multiply(y, dx), x),
            ),
        ),
    strPow,
);

const strLog = "log";
let Log = generateExpression(
    (x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)),
    (x, y, dx, dy) =>
        new Subtract(
            new Divide(dy, new Multiply(new Log(Const.E, x), y)),
            new Divide(
                new Multiply(new Divide(dx, x), new Log(Const.E, y)),
                new Multiply(new Log(Const.E, x), new Log(Const.E, x))
            )
        ),
    strLog,
);

const strMean = "mean";
let Mean = generateExpression(
    (...args) => args.reduce((partialSum, a) => partialSum + a, 0) / args.length,
    (...args) => new Mean(...args.slice(args.length / 2)),
    strMean,
);

const strVar = "var";
let Var = generateExpression(
    (...args) => (args.map(x => x ** 2).reduce((partialSum, a) => partialSum + a, 0) / args.length -
        (args.reduce((partialSum, a) => partialSum + a, 0) / args.length) ** 2),
    (...args) =>
        new Subtract(
            new Multiply(
                new Mean(...scalarProduct(args.slice(0, args.length / 2), args.slice(args.length / 2))),
                Const.TWO,
            ),
            new Multiply(
                new Multiply(
                    new Mean(...args.slice(0, args.length / 2)),
                    new Mean(...args.slice(args.length / 2))),
                Const.TWO,
            ),
        ),
    strVar,
);

function scalarProduct(a, b) {
    let res = [];
    for (let i = 0; i < Math.min(a.length, b.length); i++) {
        res.push(new Multiply(a[i], b[i]));
    }
    return res;
}

function isInt(str) {
    return !isNaN(str) && str !== "";
}

let funAndArity = (fun, art) => { return {Fun : fun, arity : art} };
const OPERATIONS = new Map([
    [strPow, funAndArity(Pow, 2)],
    [strLog, funAndArity(Log, 2)],
    [strMultiply, funAndArity(Multiply, 2)],
    [strDivide, funAndArity(Divide, 2)],
    [strAdd, funAndArity(Add, 2)],
    [strSubtract, funAndArity(Subtract, 2)],
    [strNegate, funAndArity(Negate, 1)],
    [strMean, funAndArity(Mean, Infinity)],
    [strVar, funAndArity(Var, Infinity)],
]);

function parse(string) {
    let stack = [];
    external: for (const token of string.split(" ").filter(i => !!i)) {
        for (let operation of OPERATIONS.entries()) {
            if (token === operation[0]) {
                stack.push(new operation[1].Fun(...stack.splice(stack.length - operation[1].arity)))
                continue external;
            }
        }
        stack.push(isInt(token) ? new Const(Number.parseInt(token)) : new Variable(token));
    }
    return stack.pop();
}

function ParseError(message, position) {
    this.message = `${message}${position !== undefined ? `, at position: ${position}` : ""}`;
}
ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

function NonValidExpressionError(expected, found, token, position) {
    ParseError.call(
        this,
        `Arguments expected: ${expected}, arguments found: ${found}, by operator '${token}'`,
        position,
    );
}
NonValidExpressionError.prototype = Object.create(ParseError);
NonValidExpressionError.prototype.constructor = NonValidExpressionError;

function UnknownToken(token, position) {
    ParseError.call(
        this,
        `Unknown token found '${token}'`,
        position,
    );
}
UnknownToken.prototype = Object.create(ParseError);
UnknownToken.prototype.constructor = UnknownToken;

function UnknownVariable(token, position) {
    UnknownToken.call(this, token, position);
}
UnknownVariable.prototype = Object.create(UnknownToken);
UnknownVariable.prototype.constructor = UnknownVariable;

function UnknownOperator(token, position) {
    UnknownToken.call(this, token, position);
}
UnknownOperator.prototype = Object.create(UnknownToken);
UnknownOperator.prototype.constructor = UnknownOperator;

function Source(string, direction) {
    this.string = string;
    this.shift = direction ? 0 : 1;
    this.pointer = direction ? 0 : string.length - 1;
    this.get = () => string[this.pointer];
    this.getPointer = () => this.pointer;
    this.isEmpty = () => !(0 <= this.pointer && this.pointer < string.length);
    this.emptyString = () => string.length === 0;
    this.slice = (first, last) => string.slice(first + this.shift, last + this.shift);
    this.reverse = (a) => direction ? a : a.reverse();
    this.movePointer = (i = 1) => direction ? this.pointer += i : this.pointer -= i;
}

function parsePrefix(string) {
    const source = new Source(string, true);
    return parseFix(source, "(", ")");
}

function parsePostfix(string) {
    const source = new Source(string, false);
    return parseFix(source, ")", "(");
}

function parseFix(source, openBracket, closeBracket) {
    if (source.emptyString()) {
        throw new ParseError("Empty string");
    }
    let expr = parseExpression();
    skipWhitespace();
    if (!source.isEmpty()) {
        throw new ParseError("Excessive info", source.getPointer() + 1);
    }
    return expr;

    function parseExpression() {
        skipWhitespace();
        if (expect(openBracket)) {
            skipWhitespace();
            const tokenPosition = source.getPointer() + 1;
            const token = getToken();
            skipWhitespace();
            for (const operation of OPERATIONS.entries()) {
                if (token === operation[0]) {
                    let args = [];
                    while (!expect(closeBracket)) {
                        args.push(parseExpression());
                        skipWhitespace();
                    }
                    if (operation[1].arity === Infinity || args.length === operation[1].arity) {
                        return new operation[1].Fun(...source.reverse(args));
                    }
                    throw new NonValidExpressionError(operation[1].arity, args.length, args.length, tokenPosition);
                }
            }
            throw token === "" ? new ParseError("Empty operator") : new UnknownOperator(token, tokenPosition);
        }
        return parseAtom();
    }

    function parseAtom() {
        const token = getToken();
        if (isInt(token)) {
            return new Const(Number.parseInt(token));
        }
        switch (token) {
            case strX: return Variable.X;
            case strY: return Variable.Y;
            case strZ: return Variable.Z;
            case "": throw new ParseError("Missed argument", source.getPointer() + 1);
            default:
                source.movePointer(-token.length);
                throw new UnknownVariable(token, source.getPointer() + 1);
        }
    }

    function expect (c) {
        if (source.isEmpty()) {
            throw new ParseError(`Expected token: ${c}`, source.getPointer() + 1);
        }
        if (source.get() === c) {
            source.movePointer();
            return true;
        }
        return false;
    }

    function skipWhitespace() {
        while (" \t\n\r\v".indexOf(source.get()) > -1) {
            source.movePointer();
        }
    }

    function getToken() {
        const first = source.getPointer();
        while (" \t\n\r\v()".indexOf(source.get()) <= -1 && !source.isEmpty()) {
            source.movePointer();
        }
        return source.slice(...source.reverse([first, source.getPointer()]));
    }
}
