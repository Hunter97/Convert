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

    public void loadParams( File file ) throws IOException;

    public void setChecksum() throws IOException;

    public int getChecksum();

    public void convertFile() throws IOException;

    public void writeParam( File file ) throws IOException;
}
