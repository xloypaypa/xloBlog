package model.db.virtual;

import org.bson.Document;
import org.bson.types.ObjectId;

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
            boolean flag = true;
            for (Map.Entry<String, Object> entry : filter.entrySet()) {
                if (!now.containsKey(entry.getKey()) || !now.get(entry.getKey()).equals(entry.getValue())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ans.add(now);
            }
        }
        return ans;
    }

    public void insertOne(Document t) {
        if (!t.containsKey("_id")) {
            t.put("_id", new ObjectId());
        }
        this.value.add(t);
    }

    public void deleteOne(Document filter) {
        List<Document> ans = find(filter);
        for (Document now : ans) {
            System.out.println("+"+now.toJson());
        }
        for (Document now : value) {
            System.out.println("-"+now.toJson());
        }
        for (Document now : ans) {
            System.out.println(value.contains(now));
        }
    }

    public void updateOne(Document filter, Document update) {
        List<Document> ans = find(filter);
        if (ans.size() == 0) return;
        update = (Document) update.get("$set");
        Document document = ans.get(0);
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            document.put(entry.getKey(), entry.getValue());
        }
    }
}
