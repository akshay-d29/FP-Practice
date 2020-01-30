package Scala_Cats_Exercises

import cats.Apply
import cats.implicits._

object ApplyOps extends App {

  // Apply extends Functor with new function `ap`
  // The ap function is similar to map in that we are transforming a value in a context (a context being the F in F[A]
  // The diff between ap and map is that for `ap` the function that takes care of the transformation is of type F[A => B], whereas for map it is A => B

  val optionApply: Apply[Option] = new Apply[Option] {
    override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = fa.flatMap(a => ff.map(f => f(a)))

    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa map f

    override def product[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] = fa.flatMap(a => fb.map(b => (a, b)))
  }

  val listApply: Apply[List] = new Apply[List] {
    override def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] = fa.flatMap(a => ff.map(f => f(a)))

    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa map f

    override def product[A, B](fa: List[A], fb: List[B]): List[(A, B)] = fa zip fb
  }

  //  like functors, Apply instances also compose
  val listOption = Apply[List] compose Apply[Option]
  val plusOne: Int => Int = (x: Int) => x + 1

  println(listOption.ap(List(Some(plusOne)))(List(Some(5), None, Some(3))))

  val intToString: Int => String = _.toString
  val double: Int => Int = _ * 2

  println(Apply[Option].ap(Some(intToString))(Some(55)))
  println(Apply[Option].ap(Some(double))(Some(5)))
  println(Apply[Option].ap(Some(double))(None))
  println(Apply[Option].ap(None)(Some(1)))
  println(Apply[Option].ap(None)(None))

  //  The functions `apN` (for N between 2 and 22) accept N arguments where `ap` accepts 1

  val addArity: (Int, Int) => Int = (a: Int, b: Int) => a + b
  val addArity3: (Int, Int, Int) => Int = (a: Int, b: Int, c: Int) => a + b + c

  println(Apply[Option].ap2(Some(addArity))(Some(5), Some(6)))
  println(Apply[Option].ap2(Some(addArity))(Some(5), None))

  // Similarly, `mapN` functions are available (map2, map3, ...)
  println(Apply[Option].map2(Some(4), Some(5))(addArity))
  println(Apply[Option].map3(Some(4), Some(5), Some(6))(addArity3))

  //  Similarly, `tupleN` functions are available

  println(Apply[Option].tuple2(Some(4), Some(5)))
  println(Apply[Option].tuple3(Some(4), Some(5), Some(6)))

  // Apply Builder Syntax
  //  The |@| operator offers an alternative syntax for the higher-arity Apply functions (apN, mapN and tupleN).
  //  All instances created by |@| have map, ap, and tupled methods of the appropriate arity

  // |@| symbol is deprecated.. instead use (a, b).mapN(...)

  val option2 = (Option(1), Option(3))
  val option3 = (Option(1), Option(3), Option.empty[Int])

  println(option2.mapN(addArity))
  println(option3.mapN(addArity3))

  println(option2.apWith(Some(addArity)))
  println(option3.apWith(Some(addArity3)))

  println(option2.tupled)
  println(option3.tupled)


}