package com.sezzle.dataneptune

case class Order(shopper: Shopper, merchant: Merchant, amount_in_cents: Int) {
  def validate: Unit = {
    if (!(shopper.validate && merchant.validate)) {
      throw new OrderValidationException
    }
  }
}

class OrderValidationException extends Exception("User validation exception")
