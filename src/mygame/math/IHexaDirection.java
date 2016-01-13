/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.math;

/**
 *
 * @author Jasper
 */
public interface IHexaDirection {

  boolean isUp();

  IHexaDirection goTwoClockWise();

  IHexaDirection goTwoCounterClockWise();
}
