import org.junit.jupiter.api.Test

class DemoTest {

  @Test
  def testDemo(): Unit = {
    System.setProperty("password", "test")
    System.setProperty("endpoints", "adb:8529")
    System.setProperty("dbName", "imdb")
    Demo.main(Array.empty)
  }

}
