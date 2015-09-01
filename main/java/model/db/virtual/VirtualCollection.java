package model.db.virtual;

import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xlo on 2015/9/1.
 * it's the virtual collection
 */
public class VirtualCollection {
    protected Set<Document> value = new HashSet<>();

    public long count(Document filter) {
        return find(filter).size();
    }

    public List<Document> find() {
        return value.stream().collect(Collectors.toCollection(LinkedList::new));
    }

    public List<Document> find(Document filter) {
        List<Document> ans = new LinkedList<>();
        for (Document now : value) {
            for (Map.Entry<String, Object> item : filter.entrySet()) {
                if (now.containsKey(item.getKey()) && now.get(item.getKey()).equals(item.getValue())) {
                    ans.add(now);
                    break;
                }
            }
        }
        return ans;
    }

    public void insertOne(Document t) {
        this.value.add(t);
    }

    public void deleteOne(Document filter) {
        List<Document> ans = find(filter);
        ans.forEach(value::remove);
    }

    public void updateOne(Document filter, Document update) {
        List<Document> ans = find(filter);
        ans.forEach(value::remove);
        value.add(update);
    }
}
