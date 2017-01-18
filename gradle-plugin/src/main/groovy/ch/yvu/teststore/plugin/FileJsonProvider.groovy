package ch.yvu.teststore.plugin


class FileJsonProvider {
    String filePath


    def get() {
        if (filePath == null) return null
        def file = new File(filePath)
        if (!file.exists()) return null
        return file.text
    }

}
