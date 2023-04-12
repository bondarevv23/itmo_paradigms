sieve(1).

sieve_add(N) :- sieve(N), !.
sieve_add(N) :- \+ sieve(N), assert(sieve(N)).

sift(S, F, L) :- F > L, !.
sift(S, F, L) :-
	L >= F,
	sieve_add(F),
	F1 is F + S,
	sift(S, F1, L).

check_sift(N, M) :- \+ sieve(N), !, NSQ is N * N, sift(N, NSQ, M).
check_sift(N, M) :- sieve(N).

fill_table(F, L) :- FSQ is F * F, FSQ > L, !.
fill_table(F, L) :-
	FSQ is F * F, L >= FSQ,
	check_sift(F, L),
	F1 is F + 1,
	fill_table(F1, L).

init(N) :- fill_table(1, N).

prime(N) :- \+ sieve(N).

composite(N) :- \+ (N = 1), sieve(N).

prime_divisors(1, []).
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [H | T]) :-
	number(N), !, \+ (N = 1),
	min_del(N, 2, H),
	N1 is div(N, H),
	prime_divisors(N1, T).
prime_divisors(N, [H1 | [H2 | T]]) :- 
	prime(H1), prime(H2), H2 >= H1,
	prime_divisors(N1, [H2 | T]),
	N is H1 * N1.

min_del(N, P, P) :- 0 is mod(N, P), !.
min_del(N, P, R) :- P1 is P + 1, min_del(N, P1, R).

prime_palindrome(N, K) :- number(N), prime(N), K is N - 1.
prime_palindrome(N, K) :- number(K), N is K + 1, prime(N).
prime_palindrome(N, K) :- prime(N), palindrome(N, K).

palindrome(N, K) :-
	notation(N, K, L1),
	reverse(L1, L2),
	L1 = L2.

notation(N, K, [N]) :- K > N, !.
notation(N, K, [M | L]) :- 
	D is div(N, K),
	M is mod(N, K),
	notation(D, K, L).

