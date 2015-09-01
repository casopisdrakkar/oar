package sk.drakkar.oar;

public class ReleaseDateParser {
    public ReleaseDate parse(Issue issue) {
        String pdf = issue.getPdfFileName();
        // example: drakkar_2015_51_srpen.pdf
        String[] components = pdf.split("_|\\.");
        if(components.length != 5) {
            throw new IllegalIssueFileNameException("Illegal format of issue PDF file name: " + pdf);
        }
        String year = components[1];
        String month = components[3];

        return ReleaseDate.parseFromRawCzechMonthName(Integer.parseInt(year), month);
    }
}
