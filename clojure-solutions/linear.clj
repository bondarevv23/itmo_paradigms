(defn and-list [array] (reduce #(and %1 %2) true array))
(defn neigh-elements-test [fun array]
  (and
    (coll? array)
    (or
      (>= 1 (count array))
      (and-list
        (mapv #(fun (nth array (- % 1)) (nth array %)) (range 1 (count array)))))))
(def equal-lengths (partial neigh-elements-test #(== (count %1) (count %2))))
(defn is-vector?
  ([array]
   (and
     (coll? array)
     (and-list (mapv number? array))))
  ([array element-test]
   (and
     (coll? array)
     (and-list (mapv element-test array)))))
(defn is-matrix?
  ([array]
   (and
     (is-vector? array is-vector?)
     (equal-lengths array)))
  ([array elements-test]
   (and
     (is-vector? array #(is-vector? % elements-test))
     (equal-lengths array))))
(defn is-tensor?
  ([array]
   (and
     (is-vector? array is-matrix?)
     (equal-lengths array)))
  ([array elements-test]
   (and
     (is-vector? array #(is-matrix? % elements-test))
     (equal-lengths array))))

(defn by-elements [args-tester element-tester operator & args]
  {:pre [(args-tester args element-tester)]
   :post [(or (is-matrix? % element-tester) (is-vector? % element-tester) (element-tester %))]}
  (if (< 1 (count args))
    (reduce #(mapv operator %1 %2) (first args) (rest args))
    (mapv operator (first args))))

(def v (partial by-elements is-matrix? #(or (number? %) (empty? %))))
(def v+ (partial v +))
(def v- (partial v -))
(def v* (partial v *))
(def vd (partial v /))

(defn v*s
  [vector & scalars]
  {:pre [(is-vector? vector)
         (or (nil? scalars) (is-vector? scalars))]
   :post [(is-vector? %)]}
  (mapv #(* (reduce * scalars) %) vector))

(defn scalar [& vectors]
  {:pre [(is-matrix? vectors)]
   :post [(number? %)]}
  (reduce + (reduce #(mapv * %1 %2) (first vectors) (rest vectors))))

(defn transpose [m]
  {:pre [(is-matrix? m)]
   :post [(is-matrix? %)]}
  (apply (partial mapv vector) m))

(defn m*v [m v]
  {:pre [(is-matrix? m)
         (is-vector? v)
         (== (count (nth m 0)) (count v))]
   :post [(is-vector? %)]}
  (mapv #(scalar v %) m))

(defn m*m [& args]
  {:pre [(is-vector? args is-matrix?)]
   :post [(is-matrix? %)]}
  (reduce #(mapv (partial m*v (transpose %2)) %1) (first args) (rest args)))

(def m (partial by-elements is-tensor? number?))
(def m+ (partial m v+))
(def m- (partial m v-))
(def m* (partial m v*))
(def md (partial m vd))

(defn m*s [matrix & scalars]
  {:pre [(is-matrix? matrix)
         (or (nil? scalars) (is-vector? scalars))]
   :post [(is-matrix? %)]}
  (mapv #(v*s % (reduce * scalars)) matrix))

(defn vect [& args]
  {:pre [(is-matrix? args)
         (is-vector? args #(== 3 (count %)))]
   :post [(is-vector? %)
          (== 3 (count %))]}
  (letfn [(ind [matrix i j] (nth (nth matrix i) j))
          (minor [m i]
            (apply - (mapv
                       #(* (ind m 0 (mod (+ % i) 3)) (ind m 1 (mod (+ (- 3 %) i) 3)))
                       (range 1 3))))]
    (reduce #(mapv (partial minor [%1 %2]) (range 0 3)) (first args) (rest args))))

(defn simplex-rang [simplex]
  {:pre [(or
           (and (coll? simplex) (not (empty? simplex)))
           (number? simplex))]
   :post [(number? %)]}
  (if (coll? simplex) (+ 1 (simplex-rang (nth simplex 0))) 0))

(defn simplex-size [simplex]
  {:pre [(or
           (and (coll? simplex) (not (empty? simplex)))
           (number? simplex))]
   :post [(number? %)]}
  (if (coll? simplex) (count simplex) 0))

(defn is-simplex? [simplex]
  (or
    (number? simplex)
    (is-vector? simplex)
    (and
      (is-vector? simplex is-simplex?)
      (neigh-elements-test
        #(and
           (== (simplex-size %1) (+ 1 (simplex-size %2)))
           (== (simplex-rang %1) (simplex-rang %2)))
        simplex)
      (== 1 (simplex-size (last simplex))))))

(def x
  (partial
    by-elements
    (fn [array fun]
      (and
        (is-vector? array fun)
        (neigh-elements-test
          #(and
             (== (simplex-size %1) (simplex-size %2))
             (== (simplex-rang %1) (simplex-rang %2)))
          array)))
    is-simplex?))

(defn simplex-operator [operator & args]
  (if (coll? (first args))
    (if (empty? (rest args))
      (mapv (partial simplex-operator operator) (first args))
      (reduce #(mapv (partial simplex-operator operator) %1 %2) (first args) (rest args)))
    (apply operator args)))

(def x+ (partial x (partial simplex-operator +)))
(def x- (partial x (partial simplex-operator -)))
(def x* (partial x (partial simplex-operator *)))
(def xd (partial x (partial simplex-operator /)))
