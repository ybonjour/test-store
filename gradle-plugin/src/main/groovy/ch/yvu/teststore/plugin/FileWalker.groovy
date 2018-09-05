package ch.yvu.teststore.plugin

import java.util.regex.Pattern

class FileWalker {
    String baseDir
    Pattern pattern

    def walkFileContents(visitor) {
        new File(baseDir).eachFileRecurse {
            if (it.isFile() && it.path ==~ pattern) {
                visitor(it.absolutePath, it.getText("UTF-8"))
            }
        }
    }
}
