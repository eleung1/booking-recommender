package com.eric.recommender;

/**
 * Wrapper class that represents a city along with its relevance score
 * with respect to the desired passions in the search query.
 * 
 * @author Eric Leung
 *
 */
public class CityScoreModel implements Comparable<CityScoreModel>
{
  City city;
  //Map<Passion, Long> desiredPassionsScoreMap;
  double finalScore;
  
  public CityScoreModel(City city)
  {
    this.city = city;
    finalScore = 0;
    //desiredPassionsScoreMap = new HashMap<Passion, Long>();
  }

  @Override
  public int compareTo(CityScoreModel model)
  {
    if ( model.finalScore == this.finalScore ) return 0;
    
    return model.finalScore > this.finalScore ? 1 : -1;
  }

  
}
