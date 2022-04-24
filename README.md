## campaign-api [![Scala CI](https://github.com/techmonad/campaign-api/actions/workflows/scala.yml/badge.svg)](https://github.com/techmonad/campaign-api/actions/workflows/scala.yml)

  It is sbt sample project for building restful marketing campaigns(specially for Online Travel Agency) with maintaining code quality and writing unit + integration test. It is build top of Akka HTTP. 

**Run unit test:**
```
$ sbt test
```

**Run integration test:**
```
   $sbt it:test
```


**Run the app:**
```
   $ sbt run
``` 


##Endpoint details:

1) Get welcome message
```
   $ curl localhost:9000
   response => Welcome to Campaign api!!
```


2) Disable a campaign(here campaign is HotelDealCampaign)

```
   $ curl localhost:9000/v1/HotelDealCampaign/flip?enabled=false
   response => HotelDealCampaign has been disabled successfully!
```

3) Enable a  campaign(here campaign is HotelDealCampaign)
```
   $ curl localhost:9000/v1/HotelDealCampaign/flip?enabled=true
   response => HotelDealCampaign has been enabled successfully!
```

4) Refresh  campaign detail(this will read all hotel & country list from a given file)
```
   $ curl localhost:9000/v1/HotelDealCampaign/refresh
   response => Campaign detail has been updated successfully
```

5) Get score when countryId exists in list
```
   curl -XPOST localhost:9000/v1/HotelDealCampaign/score -d '[{"hotelId":1232 ,"countryId":10}]'
   response => [{"countryId":10,"score":3}]
```

6) Get score when hotelId exists in list
```
   curl -XPOST localhost:9000/v1/HotelDealCampaign/score -d '[{"hotelId":12 ,"countryId":101010}]'
   response => [{"hotelId":12,"score":5}]
```

7) Get score when both exists in list
```
   curl -XPOST localhost:9000/v1/HotelDealCampaign/score -d '[{"hotelId":12 ,"countryId":10}]'
   response => [{"hotelId":12,"score":5}]
```



8) Get Scores of list
```
   curl -XPOST localhost:9000/v1/HotelDealCampaign/score -d '[{"hotelId":1232 ,"countryId":10},{"hotelId":12 ,"countryId":101010}, {"hotelId":12 ,"countryId":10},{"hotelId":12211 ,"countryId":101010}]'
   response => [{"countryId":10,"score":3},{"hotelId":12,"score":5},{"hotelId":12,"score":5}]
```
