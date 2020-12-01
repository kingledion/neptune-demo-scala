package com.sezzle.dataneptune

case class Merchant(id: String, dba: String) {
  def validate : Boolean = {
    id != "" && dba != ""
  }
}
