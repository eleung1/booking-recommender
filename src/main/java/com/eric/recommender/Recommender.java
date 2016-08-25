package com.eric.recommender;

import java.util.*;

/**
 * Travel recommender system.
 * 
 * Given a list of destinations and a list of passions that the destinations are known for,
 * implement a search engine that can receive as argument a list of passions:
 * For instance:
 * query: sight-seeing, museum, food
 * 
 * It should recommend a list of cities at matches those passions.
 * 
 * @author Eric Leung
 *
 */
public class Recommender
{
  // key=passion; value=list of cities known for this passion
  private Map<Passion, Set<City>> passionCityMap;
  
  // Cities known by this recommender
  private Set<City> availableCities;
  
  public Recommender()
  {
    availableCities = new HashSet<City>();
    passionCityMap = new HashMap<Passion, Set<City>>();
  }
  
  /**
   * Given a list of passions, return a list of destinations that 
   * matches those passions.
   * 
   * @param passions Desired passions
   * @return A ranked list of cities that matches the desired passions
   */
  public CityScoreModel[] search(List<Passion> passions)
  {
    Set<City> cities = new HashSet<City>();
    
    //passions.forEach(p -> cities.addAll(passionCityMap.get(p)));
    
    // find the cities which contains all desired passions
    for ( Passion p : passions ) {
      if ( cities.isEmpty() ) {
        cities.addAll(passionCityMap.get(p));
      } else {
        cities.retainAll(passionCityMap.get(p));
      }
    }
    
    // calculate tf-idf score for those cities
    List<CityScoreModel> citiesScore = tfIdf(cities, passions);
    
    // Sort descending by score
    CityScoreModel[] citiesScoreArr = citiesScore.toArray(new CityScoreModel[0]);
    Arrays.sort(citiesScoreArr);
    
    return citiesScoreArr;
  }
  
  public List<CityScoreModel> tfIdf(Set<City> cities, List<Passion> passions) {
    List<CityScoreModel> cityScoreModels = new ArrayList<CityScoreModel>();
    
    //for each city, calculate the tf-idf for each passion
    for ( City c: cities ) {
      
      // store the tf-idf score in the wrapper class CityScoreModel
      CityScoreModel model = new CityScoreModel(c);
      for ( Passion p: passions ) {
        
        // termFrequency (tf) = how often this term appears in this doc
        // i.e. how often this passion is endorsed with respect to other endorsements in this city
        long endorsement = c.getEndorsementForPassion(p);
        double tf = (double)endorsement / (double)c.getTotalEndorsements();
        
        // inverse doc frequency(idf) = Total number of docs / number of docs this term appears at least once in
        // i.e total number of city divided by number of cities with this passion
        int nt = passionCityMap.get(p).size();
        int N = this.availableCities.size();
        
        // score = termFrequency * inverse document frequency
        double score = (double)tf * Math.log((double)N/(double)nt);
        model.finalScore+=score;
      }
      
      cityScoreModels.add(model);
      
    }
    
    return cityScoreModels;
  }
  
  /**
   * Add a city to this recommender
   * 
   * @param c
   */
  public void addCity(City... cities){
    for (City c : cities){
      this.availableCities.add(c);
    }
  }
  
  public static void main(String[] args)
  {
    Recommender rc = new Recommender();
    
    // Add some cities to our recommender
    City hongKong = new City("Hong Kong");
    City toronto = new City("Toronto");
    City amsterdam = new City("Amsterdam");
    City london = new City("London");
    rc.addCity(hongKong, toronto, amsterdam, london);
    
    // Add some endorsements to our cities
    Passion walking = new Passion("Walking");
    Passion food = new Passion("Food");
    Passion museum = new Passion("Museum");
    Passion sharkDiving = new Passion("Shark Diving");
    
    hongKong.endorse(walking, 1);
    hongKong.endorse(food, 1);
    hongKong.endorse(museum, 1);
    toronto.endorse(walking, 50);
    toronto.endorse(food, 50);
    toronto.endorse(museum, 1);
    amsterdam.endorse(museum, 1000);
    london.endorse(sharkDiving, 1);  // TODO: handle bogus endorsements 
    
    // Populate the passion city map: key=passion; value= set of cities with that passion
    rc.passionCityMap.put(walking, new HashSet<City>(Arrays.asList(new City[]{hongKong, toronto})));
    rc.passionCityMap.put(food, new HashSet<City>(Arrays.asList(new City[]{hongKong, toronto})));
    rc.passionCityMap.put(museum, new HashSet<City>(Arrays.asList(new City[]{toronto, amsterdam})));
    
    // Test query: find cities that's good for walking and food
    List<Passion> query = new ArrayList<Passion>();
    query.add(walking);
    query.add(food);
    
    CityScoreModel[] citiesScoreArr = rc.search(query);
    for ( CityScoreModel c : citiesScoreArr ) 
    {
      System.out.println(c.city.name + ": " + c.finalScore);
    }
  }
}
