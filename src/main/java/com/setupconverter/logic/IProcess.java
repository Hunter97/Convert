/**
 * 
 */
package com.setupconverter.logic;

import java.io.File;
import java.io.IOException;


/**
 * @author prwallace
 *
 */
public interface IProcess {

    public void load( File file ) throws IOException;

    public void setChecksum() throws IOException;

    public int getChecksum();

    public void convert() throws IOException;

    public void write( File file ) throws IOException;
}
