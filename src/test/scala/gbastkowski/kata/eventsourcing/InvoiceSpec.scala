package gbastkowski.kata.eventsourcing

import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    "can be created" in {
      new Invoice()
    }
    "can add a recipient" in {
      val invoice = new Invoice()
      invoice.addRecipient("Recipient")
      invoice.recipients should have size 1
    }
  }
}
