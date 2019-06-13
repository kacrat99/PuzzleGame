package address.models;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Kacper Ratajczak 
 * 
 * 
 * 
 */

@XmlRootElement(name = "scores")
public class ScoreWraper {

    private List<Score> scores;

    @XmlElement(name = "score")
    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}