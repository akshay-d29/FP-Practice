//import eu.timepit.refined.types.string.NonEmptyString
//import cats.implicits._
//
//object PFP extends App {
//
//  case class User()
//
//  def lookup1(userName: String, email: String): Option[User] = ???
//
//  lookup1("aeinstein@research.com", "aeinstein")
//  lookup1("aeinstein", "123")
//  lookup1("", "")
//
//  //  ** Value Classes
//
//  case class Username(value: String) extends AnyVal
//
//  case class Email(value: String) extends AnyVal
//
//  def lookup2(username: Username, email: Email): Option[User] = ???
//
//  lookup2(Username("aeinstein"), Email("aeinstein@research.com"))
//
//  lookup2(Username("aeinstein@research.com"), Email("aeinstein"))
//  lookup2(Username("aeinstein"), Email("123"))
//  lookup2(Username(""), Email(""))
//
//  //  ** A way around this is to make the case class constructors private and give the user smart constructors.
//
//  case class Username private(val value: String) extends AnyVal
//
//  case class Email private(val value: String) extends AnyVal
//
//  def mkUsername(value: String): Option[Username] = {
//    if (value.nonEmpty) Some(Username(value)) else None[Username]
//  }
//
//  def mkEmail(value: String): Option[Email] = {
//    if (value.nonEmpty && value.contains("@")) Some(Email(value)) else None[Email]
//  }
//
//  //  ** Smart constructors are functions such as mkUsername and mkEmail which take a raw value and return an optional validated one.
//  //  The optionality can be denoted using types such as Option, Either, Validated.
//
//  (mkUsername("aeinstein"), mkEmail("aeinstein@research.com")).mapN {
//    case (username, email) => lookup2(username, email)
//  }
//
//  //  But guess what ? We can still do wrong...
//
//  (mkUsername("aeinstein"), mkEmail("aeinstein@research.com")).mapN {
//    case (username, email) => lookup2(username.copy(value = ""), email)
//  }
//
//  //  *** Newtypes newtype library gives us zero - cost wrappers with no runtime overheads related to value types(extra memory allocation)
//
//  import io.estatico.newtype.macros._
//
//  @newtype case class Username(value: String)
//
//  @newtype case class Email(value: String)
//
//  //  smart constructors are still needed to avoid invalid data
//
//  //  *** Refinement types ==> Solution over (newtype + smartconstructor boilerplate)
//
//  //  Refinement types allow us to validate data in compile time as well as in runtime
//
//  import eu.timepit.refined.api.Refined
//  import eu.timepit.refined.collection.Contains
//
//  type Username = String Refined Contains['g']
//
//  def lookup3(username: Username): Option[User]
//
//  lookup3(Username("")) // error
//  lookup3(Username("aeinstein")) // error
//  lookup3(Username("csagan")) // compiles
//
//  //  We can combine Newtypes and Refinement types together
//
//  @newtype case class Brand(value: NonEmptyString)
//
//
//}
