node(Key, Value, L_child, R_child, node(Key, S_key, Value, L_child, R_child)) :- rand_int(2000000000, S_key).
node(Key, Value, Result) :- node(Key, Value, nil, nil, Result).
key(node(Key, _, _, _, _), Key).
s_key(node(_, S_key, _, _, _), S_key).
value(node(_, _, Value, _, _), Value).
l_child(node(_, _, _, L_child, _), L_child).
r_child(node(_, _, _, _, R_child), R_child).

merge(A, B, B) :- A = nil, !.
merge(A, B, A) :- B = nil, !.
merge(node(A_key, A_skey, A_value, A_lchild, A_rchild), B, node(A_key, A_skey, A_value, A_lchild, New_A_rchild)) :-
	s_key(B, B_skey),
	A_skey > B_skey, !,
	merge(A_rchild, B, New_A_rchild).
merge(A, node(B_key, B_skey, B_value, B_lchild, B_rchild), node(B_key, B_skey, B_value, New_B_lchild, B_rchild)) :-
	s_key(A, A_skey),
	B_skey >= A_skey, !,
	merge(A, B_lchild, New_B_lchild).

split(A, K, (nil, nil)) :- A = nil, !.
split(node(A_key, A_skey, A_value, A_lchild, A_rchild), K, (node(A_key, A_skey, A_value, A_lchild, L_part), R_part)) :-
	A_key < K, !,
	split(A_rchild, K, (L_part, R_part)).
split(node(A_key, A_skey, A_value, A_lchild, A_rchild), K, (L_part, node(A_key, A_skey, A_value, R_part, A_rchild))) :-
	A_key >= K, !,
	split(A_lchild, K, (L_part, R_part)).

add(A, X, Result) :-
	key(X, X_key),
	split(A, X_key, (Fp, D1)),
	X1_key is X_key + 1,
	split(A, X1_key, (D2, Sp)),
	merge(Fp, X, H_tree),
	merge(H_tree, Sp, Result).

remove(A, K, Result) :-
	split(A, K, (Fp, _)),
	K1 is K + 1,
	split(A, K1, (_, Sp)),
	merge(Fp, Sp, Result).

find(A, K, Result) :-
	split(A, K, (_, H)),
	K1 is K + 1,
	split(H, K1, (node(_, _, Result, _, _), _)).

map_build([], nil).
map_build([(Key, Value) | Tail], Result) :-
	node(Key, Value, Node),
	map_build(Tail, TreeMap),
	add(TreeMap, Node, Result).

map_put(TreeMap, Key, Value, Result) :-
	node(Key, Value, Node),
	add(TreeMap, Node, Result).

map_remove(TreeMap, Key, Result) :- remove(TreeMap, Key, Result).

map_get(TreeMap, Key, Result) :- find(TreeMap, Key, Result).

map_maxKey(node(Key, _, _, _, nil), Key) :- !.
map_maxKey(node(Key, _, _, _, R_child), Result) :- map_maxKey(R_child, Result).

map_floorKey(Tree, Key, FloorKey) :-
	Key1 is Key + 1,
	split(Tree, Key1, (F_tree, _)),
	map_maxKey(F_tree, FloorKey).
