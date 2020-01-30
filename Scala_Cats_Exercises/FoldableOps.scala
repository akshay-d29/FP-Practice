package Scala_Cats_Exercises

import cats.implicits._
import cats.{Foldable, Later, Monoid, MonoidK, Now}

object FoldableOps extends App {

  // Foldable type class instances can be defined for data structures that can be folded to a summary value.

  // In the case of a collection (such as List or Set),
  // these methods will fold together (combine) the values contained in the collection to produce a single result.
  // Most collection types have `foldLeft` methods, which will usually be used by the associated Foldable[_] instance.

  //  Foldable[F] is implemented in terms of two basic methods:

  // foldLeft(fa, b)(f)  ====> foldLeft is an eager left-associative fold on F using the given function.

  println(Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _))

  // foldRight(fa, b)(f) ====> foldRight is a `lazy right-associative fold` on F using the given function.
  // The function has the signature (A, Eval[B]) => Eval[B] to support laziness in a stack-safe way.

  val lazyResult = Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) => Later(x + rest.value))
  println(lazyResult.value)

  // `fold` also called `combineAll`, combines every value in the foldable using the `given Monoid` instance.

  val stringMonoid: Monoid[String] = new Monoid[String] {
    override def empty: String = " "

    override def combine(x: String, y: String): String = x + empty + y
  }
  println(Foldable[List].fold(List("a", "b", "c"))(stringMonoid))
  println(Foldable[List].fold(List(1, 2, 3)))

  // `foldMap` is similar to fold but `maps every A value into B` and then combines them using the `given Monoid[B]` instance.

  println(Foldable[List].foldMap(List(1, 2, 3))(_.toString)(stringMonoid))
  println(Foldable[List].foldMap(List("a", "b", "c"))(_.length))

  val listMonoidK: MonoidK[List] = new MonoidK[List] {
    override def empty[A]: List[A] = List.empty[A]

    override def combineK[A](x: List[A], y: List[A]): List[A] = x ::: y
  }

  //  foldK is similar to fold but `combines every value in the foldable` using the `given MonoidK[G]` instance instead of Monoid[G].

  println(Foldable[List].foldK[List, Int](List(List(1, 2, 3), List(4, 5), List(6, 7)))(listMonoidK))

  println(Foldable[List].foldK(List(None, Option(5), Option(6))))

  // `find` searches for the first element matching the predicate, if one exists.

  println(Foldable[List].find(List(1, 2, 3, 4))(_ > 2))

  // `exists` checks whether at least one element satisfies the predicate.

  println(Foldable[List].exists(List(1, 2, 3, 4))(_ > 2))

  // `forall` checks whether all elements satisfy the predicate.
  println(Foldable[List].forall(List(1, 2, 3, 4))(_ >= 1))

  // toList ==> Convert F[A] to List[A].
  println(Foldable[List].toList(List(1, 2, 3)))
  println(Foldable[Option].toList(Option(5)))
  println(Foldable[Option].toList(None))

  // filter_ ==> Convert F[A] to List[A] only including the elements that match a predicate.
  println(Foldable[List].filter_(List(1, 2, 3, 4))(_ > 2))
  println(Foldable[Option].filter_(Option(5))(_ > 22))

  def parseInt(s: String): Option[Int] = Either.catchOnly[NumberFormatException](s.toInt).toOption

  // traverse_
  println(Foldable[List].traverse_(List("1","2","3"))(parseInt))
  println(Foldable[List].traverse_(List("a","v","x"))(parseInt))

  // `compose` ==> We can compose Foldable[F[_]] and Foldable[G[_]] instances to obtain Foldable[F[G]].
  val FoldableListOption = Foldable[List].compose[Option]
  println(FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4))))
  println(FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3"))))
}
