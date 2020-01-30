package Scala_Cats_Exercises

import cats.kernel.Semigroup
import cats.implicits._

object SemigroupOps extends App {
  // Semigroup has a method `combine` which is associative
  // ((a combine b) combine c) === (a combine (b combine c))

  println(Semigroup[Int].combine(1, 2))
  println(Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6)))
  println(Semigroup[Option[Int]].combine(Option(1), None))
  println(Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6))

  val aMap = Map("foo" -> Map("bar" -> 5))
  val anotherMap = Map("foo" -> Map("bar" -> 6))
  val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(aMap, anotherMap)
  println(combinedMap)
}