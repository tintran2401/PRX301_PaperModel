/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.utils.xmlchecker;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author TinTT
 */
public class TestChecker {
    public static void main(String[] args) {
        XmlSyntaxChecker checker = new XmlSyntaxChecker();
        
        Map<String, String> map = new LinkedHashMap<>();
        map.put("non value attribute", "<h1 checked>YEAH</h1>");
        map.put("non quoted value attribute", "<h1 aa =   aa><img src=src/>YEAH</h1>");
        map.put("non space between attributes", "<h1 aa=\"aa\"bb='bb'cc=33>YEAH</h1>");
        map.put("empty element", "<h1><img src=\"\"/><br><hr />YEAH</h1>");
        map.put("error open close tags", "<li><a>Sach moi</li></a></h2>");
        
        map.entrySet().forEach((entry) -> {
            System.out.println(entry.getKey());
            
            System.out.println(entry.getValue());
            System.out.println(checker.check(entry.getValue()));
            
            System.out.println();
        });
    }
}
