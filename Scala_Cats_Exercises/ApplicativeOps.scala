package Scala_Cats_Exercises

import cats.Applicative
import cats.implicits._

object ApplicativeOps extends App {

  // Applicative extends Apply by adding a single method, `pure`
  // def pure[A](x: A): F[A] => This method takes any value and returns the value in the context of the functor

  println(Applicative[Option].pure(1))
  println(Applicative[List].pure(1))

  //  Like Functor and Apply, Applicative functors also compose naturally with each other

  // When you compose one Applicative with another,
  // the resulting pure operation will lift the passed value into one context,
  // and the result into the other context:

  println((Applicative[List] compose Applicative[Option]).pure(1)) // List(Some(1))

  //  Applicative is a generalization of Monad

}
