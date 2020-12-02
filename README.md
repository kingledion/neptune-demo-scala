# neptune-demo-scala

## How to set up EC2 to load and talk to Neptune

 - Create a Neptune instance
 - Set Neptune security rules to allow incoming connections from 8182 within a given security group
 - Create an EC2 instance using the same security group (don't get a micro, it keeps running out of memory)
 - Set EC2 security rules to allow anyone to hit your instance on port 8081 (this is the port used by the app in this repo)

### Setup EC2 instance with required code

`sudo yum install java-1.8.0-openjdk`

`curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo`

`sudo yum install sbt`

`sudo yum install git`

Clone this repo with git

### How to execute

To run REPL: 
`sbt run` (will take a while to get maven artifacts)

### Post an order using Postman (you can test on mine until I shut it down)

`POST ec2-18-191-107-198.us-east-2.compute.amazonaws.com:8081/order`

```
{
    "shopper": {
        "id": "8315ef05-b180-4a6a-8e48-3e228dda52aa",
        "firstname": "Aloisious",
        "lastname": "Dingleberry"
    },
    "merchant": {
        "id": "a116af67-d0a2-45b6-a726-dc17a7d5efb4",
        "dba": "A Quiver Full"
    },
    "amount_in_cents": 1000
}
```

### Get recommendations based on graph

`GET ec2-18-191-107-198.us-east-2.compute.amazonaws.com:8081/recommend/8315ef05-b180-4a6a-8e48-3e228dda52aa`

The algorithm is the stupidest possible recommendation engine: for all stores you shopped at, for all other shoppers who shopped at those stores, get all stores 
those other shoppers shopped at, not sorted by any weights. 

There is no error handling! Do it right or it won't work!


