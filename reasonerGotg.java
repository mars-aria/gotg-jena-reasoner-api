package assignment2;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

public class reasonerGotg {

    public static void main(String[] args) {
        // Load RDF data from file
        Model model = ModelFactory.createDefaultModel();
        FileManager.get().readModel(model, "src/assignment2/gotg2.rdf");

        // Setting up rules
        String rules = """
                [r1:
                    (?x http://www.mcu.fake/siteinfo/gotg/relationships/loverOf ?y),
                    (?x http://www.mcu.fake/siteinfo/gotg/relationships/sisterOf ?z)
                    -> (?z http://www.mcu.fake/siteinfo/gotg/relationships/friendOf ?y)
                ]
                """;

        /* Query String */
        String queryString = "PREFIX rela: <http://www.mcu.fake/siteinfo/gotg/relationships/>"
                + "SELECT ?guardian WHERE { ?guardian rela:friendOf ?y . }";

        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        InfModel inf = ModelFactory.createInfModel(reasoner, model);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qe = QueryExecutionFactory.create(query, inf)) {
            ResultSet results = qe.execSelect();

            /* Output result */
            ResultSetFormatter.out(System.out, results, query);
        }
    }
}
