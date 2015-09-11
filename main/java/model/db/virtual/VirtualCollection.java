package model.db.virtual;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xlo on 2015/9/1.
 * it's the virtual collection
 */
public class VirtualCollection {
    protected Map<String, Document> value = new HashMap<>();

    public long count(Document filter) {
        return find(filter).size();
    }

    public List<Document> find() {
        return value.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<Document> find(Document filter) {
        List<Document> ans = new LinkedList<>();
        for (Map.Entry<String, Document> nowEntry : value.entrySet()) {
            Document now = nowEntry.getValue();
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
        this.value.put(t.getObjectId("_id").toString(), t);
    }

    public void deleteOne(Document filter) {
        List<Document> ans = find(filter);
        if (ans.size() == 0) return;
        this.value.remove(ans.get(0).getObjectId("_id").toString());
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
