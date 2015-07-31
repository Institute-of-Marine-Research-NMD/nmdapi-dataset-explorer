package no.imr.nmdapi.datasetexplorer.dao.mapper;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 *
 * @author kjetilf
 */
public class MissionNamespacePrefixMapper extends NamespacePrefixMapper {

    public static final String BIOTIC_NS = "http://www.imr.no/formats/nmdmission/v1";

    @Override
    public String getPreferredPrefix(String namespaceUri,
                               String suggestion,
                               boolean requirePrefix) {
        return "";
    }

}
