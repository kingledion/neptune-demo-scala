# neptune-demo-scala

## How to set up EC2 to load and talk to Neptune

 - Create a Neptune instance
 - Set Neptune security rules to allow incoming connections from 8182 within a given security group
 - Create an EC2 instance using the same security group

### Setup EC2 instance

`sudo yum install java-1.8.0-openjdk`

`curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo`

`sudo yum install sbt`

`sudo yum install git`

Clone this repo with git

### How to run

To run REPL: 
`sbt run` (will take a while to get maven artifacts)


