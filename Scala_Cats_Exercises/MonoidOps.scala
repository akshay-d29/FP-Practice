package Scala_Cats_Exercises

import cats.kernel.Monoid
import cats.implicits._

object MonoidOps extends App {
  // Monoid extends Semigroup with an `empty` method
  // (combine(x, empty) == combine(empty, x) == x)

  println(Monoid[String].combineAll(List("a", "b", "c")))
  println(Monoid[String].combineAll(List.empty[String]))

  println(Monoid[Map[String, Int]].combineAll(List.empty))
  println(Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3))))

  val l = List(1, 2, 3, 4, 5)
  println(l.foldMap(identity))
  println(l.foldMap(_.toString))

  // Foldable's foldMap maps over values accumulating the results, using the available Monoid for the type mapped onto.

  def monoidTuple[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    override def empty: (A, B) = (Monoid[A].empty, Monoid[B].empty)

    override def combine(x: (A, B), y: (A, B)): (A, B) = {
      val (xa, xb) = x
      val (ya, yb) = y
      (Monoid[A].combine(xa, ya), Monoid[B].combine(xb, yb))
    }
  }

  println(l.foldMap(i => (i, i.toString))(monoidTuple))

}