package Scala_Cats_Exercises

import cats.Functor
import cats.implicits._

object FunctorOps extends App {
  // A Functor is a ubiquitous type class involving types that have one "hole",
  // i.e. types which have the shape F[?], such as Option, List and Future.
  // (This is in contrast to a type like Int which has no hole, or Tuple2 which has two holes (Tuple2[?,?])).

  //    The Functor category involves a single operation, named map:
  //    def map[A, B](fa: F[A])(f: A => B): F[B]


  def optionFunctor: Functor[Option] = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa map f
  }

  def listFunctor: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa map f
  }

  implicit def function1Functor[In]: Functor[Function1[In, *]] = new Functor[Function1[In, *]] {
    override def map[A, B](fa: Function1[In, A])(f: A => B): Function1[In, B] = fa andThen f
  }

  val a = (x: Int) => x * 5
  println(function1Functor.map(a)(_ + 2)(5))

  println(Functor[List].map(List("abcd", "pqrs", "xyz"))(_.length))

  // Derived Methods on Functor

  // lift => We can use Functor to "lift" a function from A => B to F[A] => F[B]

  val b: String => Int = (input: String) => input.length
  val optionOfB: Option[String] => Option[Int] = Functor[Option].lift(b)

  println(optionOfB(Some("gg")))

  // fproduct => function which pairs a value with the result of applying a function to that value.

  val source = List("Cats", "is", "awesome")
  val product = Functor[List].fproduct(source)(_.length).toMap

  println(product)

  // compose => Functors compose! Given any functor F[_] and any functor G[_] we can create a new functor F[G[_]] by composing them

  val listOpt = Functor[List] compose Functor[Option]
  println(listOpt.map(List(Some(5), None, Some(3)))(_ + 5))


}