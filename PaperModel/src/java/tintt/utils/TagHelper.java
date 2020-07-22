/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.utils;

import java.util.UUID;

/**
 *
 * @author TiTi
 */
public class TagHelper {
    public synchronized static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
