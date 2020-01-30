object Composition extends App {
  	def zipList[A, B](la: List[A], lb: List[B]): List[(A, B)] = {
  		la match {
  			case head :: tl => lb.map((head, _)) ++ zipList(tl, lb)
  			case Nil => Nil
  		}
  	}

	def pureList[A](a: A): List[A] = List(a)

  	println(zipList(List(1,2,3), List("a","b","c")))

  	def zipOption[A, B](oa: Option[A], ob: Option[B]): Option[(A, B)] = {
  		(oa, ob) match {
  			case (Some(a), Some(b)) => Some(a, b)
  			case _ => None
  		}
  	}

  	def pureOption[A](a: A): Option[A] = Some(a)

  	println(zipOption(Some(1), Some("a")))

  	def zipFunction[A, B, X](f: X => A, g: X => B): X => (A, B) = {
  		(x: X) => (f(x), g(x))
  	}

  	def pureFunction[A, X](a: A): X => A = (_: X) => a

	// abstracting all the above zips into a abstract type
  	// same as Applicatives
  	// composing independent programs (F[_] = program)
  	trait Monoidal[F[_]] {
  		def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  		def pure[A](a: A): F[A]
  		// def map[A, B](fa: F[A])(f: A => B): F[B]
  	}

  	// composing dependent programs
  	// (F[A], A => F[B]): F[B]

  	def flatMapOption[A, B](oa: Option[A], f: A => Option[B]): Option[B] = {
  		oa match {
  			case None => None
  			case Some(value) => f(value)
  		}
  	}

  	def flatMapList[A, B](la: List[A], f: A => List[B]): List[B] = {
  		la match {
  			case Nil => Nil
  			case head :: tl => f(head) ++ flatMapList(tl, f)
  		}
  	}  	

  	def flatMapFunction[A, B, X](fa: X => A, f: A => (X => B)): X => B = {
  		// program reading from some context where context being X
  		(x: X) => f(fa(x))(x)
  	}

	// abstracting all the above flatMaps into a abstract type
	// composing dependent programs
	trait Monad[F[_]] {
		def flatMap[A, B](fa: F[A], f: A => F[B]): F[B]
		def pure[A](a: A): F[A]
	}

	// trait Monad[F[_]] extends Monoidal[F]
	// Anything that can be a Monad can be a perfectly fine Applicative













}