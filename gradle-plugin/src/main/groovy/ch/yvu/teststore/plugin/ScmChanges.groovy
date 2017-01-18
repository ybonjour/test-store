package ch.yvu.teststore.plugin

import groovy.json.JsonSlurper

// This class currently is hg specific
class ScmChanges {
    private FileJsonProvider jsonProvider

    def getChanges() {
        JsonSlurper slurper = new JsonSlurper()
        def changes = slurper.parseText(jsonProvider.get())
        List<ScmChange> scmChanges = new LinkedList<>();
        for (def hgChange : changes ){
            ScmChange scmChange = new ScmChange(
                    revision: hgChange.node,
                    author: hgChange.user,
                    description: hgChange.desc,
                    date: getDate(hgChange)
            )
            scmChanges.add(scmChange)
        }
        return scmChanges
    }

    private static Date getDate(hgChange) {
        if(hgChange.date == null) return null
        return new Date((hgChange.date.get(0) - hgChange.date.get(1)) * 1000L)
    }


    static class ScmChange {
        String revision
        String author
        String description
        Date date
    }

}
