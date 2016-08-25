package com.eric.recommender;

import java.util.*;

/**
 * Model of a city in our recommender system.
 * 
 * @author Eric Leung
 *
 */
public class City
{
  String name;
  
  private long totalEndorsements; // for calculating tf-idf
  
  // key=passion; value=number of endorsements for this passion
  private Map<Passion, Long> passionEndorsementMap;
  
  public City(String s)
  {
    name = s;
    passionEndorsementMap = new HashMap<Passion, Long>();
  }
  
  public void endorse(Passion p, long n) {
    if ( passionEndorsementMap.containsKey(p) ) {
      long currentEndorsements = passionEndorsementMap.get(p);
      passionEndorsementMap.put(p, currentEndorsements + n);
    } else {
      passionEndorsementMap.put(p, n);
    }
    totalEndorsements += n;
  }
  
  public long getTotalEndorsements() {
    return totalEndorsements;
  }
  
  public long getEndorsementForPassion(Passion p) {
    return passionEndorsementMap.get(p);
  }
}
