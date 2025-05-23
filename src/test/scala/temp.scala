package temp

trait Base:
  def hex: String

trait A(foo: String, bar: String) extends Base:
  override def hex = foo + bar
  
trait B(foo: String, bar: String) extends Base:
  override def hex = foo + bar

class C extends A("foo", "bar") with B("bar", "foo")