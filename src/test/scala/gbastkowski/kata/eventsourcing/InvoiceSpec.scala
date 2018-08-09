package gbastkowski.kata.eventsourcing

import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    "can be created" in {
      new Invoice()
    }
  }
}
