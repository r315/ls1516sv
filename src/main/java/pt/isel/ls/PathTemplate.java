package pt.isel.ls;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pedro on 06/04/16.
 */
public class PathTemplate extends LinkedList<PathTemplate.Segment> {

    public interface Segment {
        MatchResult match(String seg);
    }

    public PathTemplate(String pathString) throws InvalidPathException {
        super(mapIntoPathTemplateSegments(Paths.splitIntoSegments(pathString)));
    }

    public Optional<Map<String,String>> match(Path p){
        if(p.size() != this.size()){
            return null;
        }
        LinkedList<MatchResult> acc = new LinkedList<>();
        for(int i = 0 ; i<this.size() ; ++i){
            MatchResult res = this.get(i).match(p.get(i));
            if(!res.isMatch()){
                return Optional.empty();
            }
            acc.add(res);
        }
        Map<String,String> map = new HashMap<>();
        for(MatchResult res : acc){
            res.addTo(map);
        }
        return Optional.of(map);
    }

    private static List<Segment> mapIntoPathTemplateSegments(List<String> segs){
        return segs.stream().map((s) -> mapIntoPathTemplateSegment(s))
                .collect(Collectors.toList());
    }

    private static Segment mapIntoPathTemplateSegment(final String s){
        return s.startsWith("{") && s.endsWith("}") ?
                seg -> new MatchResult(true) {
                    @Override
                    public void addTo(Map<String, String> vars) {
                        vars.put(s.substring(1, s.length()-1), seg);
                    }
                }
                : seg -> new MatchResult(seg.equals(s)) {
                    @Override
                    public void addTo(Map<String, String> vars) {
                        // nothing to add
                    }
                };
    }
}
