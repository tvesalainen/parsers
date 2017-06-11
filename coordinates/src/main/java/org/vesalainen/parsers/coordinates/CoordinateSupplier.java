/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.coordinates;

/**
 *
 * @author tkv
 */
@FunctionalInterface
public interface CoordinateSupplier<T>
{
    T supply(String name, double lat, double lon);
}
