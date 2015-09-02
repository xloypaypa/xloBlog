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
            ans.addAll(filter.entrySet().stream().filter(item ->
                    now.containsKey(item.getKey()) && now.get(item.getKey()).equals(item.getValue())).map(item -> now)
                    .collect(Collectors.toList()));
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
        ans.forEach(value::remove);
    }

    public void updateOne(Document filter, Document update) {
        List<Document> ans = find(filter);
        if (ans.size() == 0) return;
        value.remove(ans.get(0));
        update.put("_id", ans.get(0).get("_id"));
        value.add(update);
    }
}
