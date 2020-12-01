package com.sezzle.dataneptune

case class Shopper(id: String, firstname: String, lastname: String) {
  def validate: Boolean = {
    id != "" && firstname != "" && lastname != ""
  }
}
