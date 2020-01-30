package Scala_Cats_Exercises

import cats.{Applicative, Monad}
import cats.implicits._

object MonadOps extends App {


  // Monad extends the Applicative type class with a new function `flatten`
  // Flatten takes a value in a nested context (eg. F[F[A]] where F is the context)
  // and "joins" the contexts together so that we have a single context (ie. F[A]).

  println(List(List(1), List(2, 3)).flatten)
  println(Option(Option(1)).flatten)

  implicit def myOptionMonad(implicit app: Applicative[Option]): Monad[Option] = new Monad[Option] {
    override def pure[A](x: A): Option[A] = app.pure(x)

    //  We can use `flatten` to define `flatMap`: `flatMap` is just `map` followed by `flatten`
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = app.map(fa)(f).flatten

    override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] = f(a) match {
      case None => None
      case Some(Left(value)) => tailRecM(value)(f)
      case Some(Right(value)) => Some(value)
    }
  }

  println(Monad[Option].pure(42))

  // IFM
  // Monad provides the ability to choose later operations in a sequence based on the results of earlier ones.
  // This is embodied in `ifM`, which lifts an if statement into the monadic context.

  println(Monad[Option].ifM(Option(false))(Option("truthy"), Option("falsy")))
  println(Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4))) // true -> 1,2 false -> 3,4 true -> 1,2

  def ifMonadicCondition(fa: Option[Boolean])(ifTrue: => Option[String], ifFalse: => Option[String]): Option[String] = fa match {
    case Some(value) => if (value) ifTrue else ifFalse
    case None => None
  }

  val gg1 = () => {
    println("hello gg1");
    Some("true")
  }
  val gg2 = () => {
    println("hello gg2");
    Some("false")
  }


  println(ifMonadicCondition(Option(true))(gg1(), gg2()))

  // Monad Transformer
  case class OptionT[F[_], A](value: F[Option[A]])

  implicit def optionTMonad[F[_]](implicit F: Monad[F]) = {
    new Monad[OptionT[F, ?]] {
      def pure[A](a: A): OptionT[F, A] = OptionT(F.pure(Some(a)))

      def flatMap[A, B](fa: OptionT[F, A])(f: A => OptionT[F, B]): OptionT[F, B] =
        OptionT {
          F.flatMap(fa.value) {
            case None => F.pure(None)
            case Some(a) => f(a).value
          }
        }

      def tailRecM[A, B](a: A)(f: A => OptionT[F, Either[A, B]]): OptionT[F, B] = ???
    }
  }

  println(optionTMonad[List].pure(42))

}
