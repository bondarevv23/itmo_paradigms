"use strict";

let cnst = value => () => value;

// :NOTE: eval
let variable = name => (x, y, z) => eval(name);

let multiVariablesExpression = f => {
    // :NOTE: Число переменных
    const fun = (...args) => (x, y, z) => f(...args.map(a => a(x, y, z)));
    fun.num = f.length;
    return fun;
};

let negate = multiVariablesExpression(x => -x);

let abs = multiVariablesExpression(Math.abs);

let add = multiVariablesExpression((x, y) => x + y);

let subtract = multiVariablesExpression((x, y) => x - y);

let multiply = multiVariablesExpression((x, y) => x * y);

let divide = multiVariablesExpression((x, y) => x / y);

let iff = multiVariablesExpression((a, b, c) => a >= 0 ? b : c);

let x = variable("x");

let y = variable("y");

let z = variable("z");

let pi = cnst(Math.PI);

let e = cnst(Math.E);

const OPERATIONS = {
    iff: iff
};

function parse(string) {
    let stack = [];
    for (const token of string.split(" ").filter(i => !!i)) {
        switch (token) {
            // :NOTE: Объект
            case "iff": applyMultiOperation(stack, iff); break;
            case "*": applyMultiOperation(stack, multiply); break;
            case "/": applyMultiOperation(stack, divide); break;
            case "+": applyMultiOperation(stack, add); break;
            case "-": applyMultiOperation(stack, subtract); break;
            case "negate": applyMultiOperation(stack, negate); break;
            case "abs": applyMultiOperation(stack, abs); break;
            // :NOTE: eval
            default: stack.push(typeof eval(token) === "function" ? eval(token) : cnst(eval(token)));
        }
    }
    return stack.pop();
}

let applyMultiOperation = (stack, f) => stack.push(f(...stack.splice(- f.num)));

let expr1 = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            cnst(2),
            variable("x")
        )
    ),
    cnst(1)
);

for (let i = 0; i <= 10; i += 1) {
    console.log(expr1(i, 0, 0));
}
