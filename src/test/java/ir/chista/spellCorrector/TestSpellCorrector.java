package ir.chista.spellCorrector;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by root on 1/22/17.
 */
public class TestSpellCorrector {
    @Test
    public void editlistTest(){

        int num_alefba=Norvig.alefba.length;
        String word="دوو";
        Norvig norvigTester=new Norvig();
        String ans=norvigTester.getAnswer(word);
       int res_delete=word.length();
       int res_transpose=word.length()-1;
       int res_insert=(word.length()+1)*num_alefba;
       int res_replace=word.length()*num_alefba;
       int res_edite=res_delete+res_insert+res_replace+res_transpose;
       assertEquals("result: ",res_edite,norvigTester.edit_List.size());
       assertEquals("result: ","دو",ans);
    }

}
