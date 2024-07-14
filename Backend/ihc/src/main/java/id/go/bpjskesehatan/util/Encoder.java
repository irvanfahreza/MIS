/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.go.bpjskesehatan.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author bambangpurwanto
 */
public interface Encoder {

    int encode(byte[] data, int off, int length, OutputStream out) throws IOException;

    int decode(byte[] data, int off, int length, OutputStream out) throws IOException;

    int decode(String data, OutputStream out) throws IOException;

}
