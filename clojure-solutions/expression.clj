(load-file "parser.clj")
(load-file "proto.clj")

(def constant constantly)

(defn variable [string]
  (fn [map] (get map string)))

(defn make-function [operator]
  (fn [& args] (fn [map] (apply operator (mapv #(% map) args)))))

(defn divide-operator
  ([arg] (/ 1.0 arg))
  ([f-arg & r-args] (/ (double f-arg) (apply * r-args))))

(defn mean-operator [& args]
  (/ (apply + args) (count args)))

(defn varn-operator [& args]
  (let [mean-v (apply mean-operator args)]
    (- (apply mean-operator (mapv #(* % %) args)) (* mean-v mean-v))))

(def add (make-function +))
(def subtract (make-function -))
(def negate subtract)
(def multiply (make-function *))
(def divide (make-function divide-operator))
(def mean (make-function mean-operator))
(def varn (make-function varn-operator))

(defn parser [string map variab cons]
  (letfn [(parse [token]
            (cond
              (list? token) (apply (map (first token)) (mapv parse (rest token)))
              (symbol? token) (variab (name token))
              (number? token) (cons token)))]
    (parse (read-string string))))

(def functions {'+ add '- subtract '* multiply '/ divide 'negate negate 'mean mean 'varn varn})
(defn parseFunction [string]
  (parser string functions variable constant))


(def _evaluate (method :evaluate))
(def _toString (method :toString))
(def _toStringInfix (method :toStringInfix))
(def _diff (method :diff))

(def AtomProto
  {:toString (fn [this] (str (proto-get this :field)))
   :toStringInfix (fn [this] (str (proto-get this :field)))})
(defn CAtom [this field] (assoc this :field field))
(def Atom (constructor CAtom AtomProto))

(declare Constant)
(def ConstantProto
  {:prototype AtomProto
   :evaluate (fn [this vars] (proto-get this :field))
   :diff (fn [this name] (Constant 0))
   })
(def Constant (constructor CAtom ConstantProto))

(def VariableProto
  {:prototype AtomProto
   :evaluate (fn [this vars] (get vars (str (Character/toLowerCase (first (proto-get this :field)))) 0))
   :diff (fn [this name] (if (= name (proto-get this :field)) (Constant 1) (Constant 0)))
   })
(def Variable (constructor CAtom VariableProto))

(def ExpressionProto
  {:toString (fn [this]
               (str "(" (proto-get this :string-operator) " "
                 (clojure.string/join " " (mapv #(_toString %) (proto-get this :args))) ")"))
   :toStringInfix (fn [this]
                    (str "(" (_toStringInfix (first (proto-get this :args))) " " (proto-get this :string-operator) " "
                         (clojure.string/join " " (mapv #(_toStringInfix %) (rest (proto-get this :args)))) ")"))
   :evaluate (fn [this vars]
               (apply (proto-get this :operator) (mapv #(_evaluate % vars) (proto-get this :args))))
   :diff (fn [this name]
           ((proto-get this :diff-operator) (proto-get this :args) (mapv #(_diff % name) (proto-get this :args))))
   })
(defn CExpression [this string-operator operator diff-operator & args]
  (assoc this :string-operator string-operator
              :operator operator
              :diff-operator diff-operator
              :args args))
(def Expression (constructor CExpression ExpressionProto))

(def NegateProto
  {:prototype ExpressionProto
   :toStringInfix (fn [this]
     (str (proto-get this :string-operator) "(" (_toStringInfix (first (proto-get this :args))) ")"))
   })
(def Negate
  (partial (constructor CExpression NegateProto)
    "negate"
    -
    (fn [args d-args] (Negate (first d-args)))))

(def Add
  (partial Expression
    "+"
    +
    (fn [args d-args] (apply Add d-args))))

(def Subtract
  (partial Expression
    "-"
    -
    (fn [args d-args] (apply Subtract d-args))))

(declare Multiply)
(defn make-d-matrix [args d-args]
  (mapv
    (fn [n]
      (apply Multiply
        (mapv #(if (== n %) (nth d-args %) (nth args %)) (range (count args)))))
    (range (count args))))

(def Multiply
  (partial Expression
    "*"
    *
    (fn [args d-args] (apply Add (make-d-matrix args d-args)))))

(def Divide
  (partial Expression
    "/"
    divide-operator
    (fn [args d-args]
      (if (== 1 (count args))
        (Divide (Negate (first d-args)) (Multiply (first args) (first args)))
        (Divide
          (apply Subtract (make-d-matrix args d-args))
          (Multiply (apply Multiply (rest args)) (apply Multiply (rest args))))))))

(def Mean
  (partial Expression
    "mean"
    mean-operator
    (fn [args d-args]
      (Divide
        (apply Add d-args)
        (Constant (count args))))))

(def Varn
  (partial Expression
    "varn"
    varn-operator
    (fn [args d-args]
      (Subtract
        (Multiply (apply Mean (mapv #(Multiply (nth args %) (nth d-args %)) (range (count args)))) (Constant 2.0))
        (Multiply
          (Multiply (apply Mean args) (apply Mean d-args))
          (Constant 2.0))))))

(def IPow
  (partial
    Expression
    "**"
    #(Math/pow %1 %2)
    (constantly nil)))

(def ILog
  (partial
    Expression
    "//"
    #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))
    (constantly nil)))

(def toString _toString)
(def toStringInfix _toStringInfix)
(def evaluate _evaluate)
(def diff _diff)

(def objects {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'mean Mean 'varn Varn})
(defn parseObject [string]
  (parser string objects Variable Constant))


(def *space (+char " \n\t\r"))

(def *ws (+ignore (+star *space)))

(def *digit (+char "0123456789"))

(defn make-constant [sign integer dot fraction]
  (letfn [(number [] (str (apply str integer) dot (apply str fraction)))
          (constant [s] (Constant (read-string s)))]
    (if (#{\- \+} sign)
      (constant (str sign (number)))
      (constant (number)))))

(def *constant (+seqf make-constant (+opt (+char "-+")) (+plus *digit) (+char ".") (+plus *digit)))

(def *variable (+map (comp Variable (partial apply str)) (+plus (+char "xyzXYZ"))))

(defn *word [[& cs]] ((partial apply +seq) (map #(+char (str %)) cs)))

(def *atom (+or *constant *variable))

(defn make-u-expr [operator expr]
  (let [u-operators {"negate" Negate}]
    (if (u-operators operator)
      ((get u-operators operator) expr))))

(declare *binary-expr)
(def *bracket
  (+or
; :NOTE: 3
    (+seqn 1 (+char "(") *ws (delay (*binary-expr 3)) *ws (+char ")"))
    *atom))

(def *unary-expr
  (+or
    (+seqf make-u-expr (+map (partial apply str) (*word "negate")) *ws (delay *unary-expr))
    (delay *bracket)))

; :NOTE: Вектор
(defn *operator [n]
  (cond
    (== n 1) (+map (partial apply str) (+or (*word "**") (*word "//")))
    (== n 2) (+or (+char "*") (+char "/"))
    (== n 3) (+or (+char "+") (+char "-"))))

(defn make-b-expr-left [expr pairs]
  (let [b-operators-left {\+ Add \- Subtract \* Multiply \/ Divide}]
    ; :NOTE: Упростить
    (reduce #((get b-operators-left (nth %2 0)) %1 (nth %2 1)) expr pairs)))

(defn make-b-expr-right [expr pairs]
  (let [b-operators-right {"**" IPow "//" ILog}]
    (if (empty? pairs)
      expr
; :NOTE: Упростить
      ((get b-operators-right (nth (first pairs) 0)) expr (make-b-expr-right (nth (first pairs) 1) (rest pairs))))))

(defn *binary-expr [n]
  (if (zero? n)
    *unary-expr
    ; :NOTE: Не там
    (+seqf (if (== n 1) make-b-expr-right make-b-expr-left)
           (delay (*binary-expr (dec n)))
           (+star (+seq *ws (*operator n) *ws (delay (*binary-expr (dec n))))))))

; :NOTE: 3
(def parseObjectInfix (+parser (+seqn 0 *ws (*binary-expr 3) *ws)))
