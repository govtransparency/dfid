package eu.dfid.server;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.dl.server.BaseServer;
import eu.dfid.dataaccess.dao.CleanProjectDAO;
import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dl.dataaccess.dao.CleanTenderDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.server.JsonTransformer;
import eu.dl.server.exceptions.NotFoundException;

import static spark.Spark.get;

/**
 * Main class for API.
 *
 * @author Kuba Krafka
 */
public final class DfidApi extends BaseServer {

    private static final String VERSION = "2.0";

    private static final String WORKER = "DfidApi";

    private static TransactionUtils transactionUtils;

    private static CleanTenderDAO cleanTenderDao;

    private static CleanProjectDAO<DFIDCleanProject> cleanProjectDao;

    private ObjectMapper mapper;

    @Override
    public void start() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        transactionUtils = DAOFactory.getDAOFactory().getTransactionUtils();

        cleanTenderDao = DAOFactory.getDAOFactory().getCleanTenderDAO(WORKER, VERSION);

        cleanProjectDao = DAOFactory.getDAOFactory().getCleanProjectDAO(WORKER, VERSION);

        registerCleanTenderEndpoints();

        registerCleanProjectEndpoints();
    }

    /**
     * Registers clean tenders endpoints.
     */
    private static void registerCleanTenderEndpoints() {
        get("/protected/clean_tender/id/:id", "application/json", (request, response) -> {
            String id = request.params("id");
            transactionUtils.begin();
            CleanTender result = cleanTenderDao.getById(id);
            if (result == null) {
                throw new NotFoundException("No such tender found. Id: " + id);
            }
            transactionUtils.commit();
            return result;
        }, new JsonTransformer());

        get("/protected/clean_tender/timestamp/:timestamp/page/:page", "application/json", (request, response) -> {
            LocalDateTime timestamp = getDate(request.params(":timestamp"));
            Integer page = getInteger(request.params(":page"));
            transactionUtils.begin();
            List<CleanTender> result = cleanTenderDao.getModifiedAfter(timestamp, page);
            transactionUtils.commit();
            return result;
        }, new JsonTransformer());

        get("/protected/clean_tender/timestamp/:timestamp/source/:source/page/:page", "application/json", (request, response) -> {
            LocalDateTime timestamp = getDate(request.params(":timestamp"));
            Integer page = getInteger(request.params(":page"));
            String source = request.params(":source");
            transactionUtils.begin();
            List<CleanTender> result = cleanTenderDao.getModifiedAfter(timestamp, source, page);
            transactionUtils.commit();
            return result;
        }, new JsonTransformer());
    }

    /**
     * Registers clean projects endpoints.
     */
    private static void registerCleanProjectEndpoints() {
        get("/protected/clean_project/id/:id", "application/json", (request, response) -> {
            String id = request.params("id");
            transactionUtils.begin();
            DFIDCleanProject result = cleanProjectDao.getById(id);
            if (result == null) {
                throw new NotFoundException("No such tender found. Id: " + id);
            }
            transactionUtils.commit();
            return result;
        }, new JsonTransformer());

        get("/protected/clean_project/timestamp/:timestamp/page/:page", "application/json", (request, response) -> {
            LocalDateTime timestamp = getDate(request.params(":timestamp"));
            Integer page = getInteger(request.params(":page"));
            transactionUtils.begin();
            List<DFIDCleanProject> result = cleanProjectDao.getModifiedAfter(timestamp, page);
            transactionUtils.commit();
            return result;
        }, new JsonTransformer());

        get("/protected/clean_project/timestamp/:timestamp/source/:source/page/:page", "application/json", (request, response) -> {
            LocalDateTime timestamp = getDate(request.params(":timestamp"));
            Integer page = getInteger(request.params(":page"));
            String source = request.params(":source");
            transactionUtils.begin();
            List<DFIDCleanProject> result = cleanProjectDao.getModifiedAfter(timestamp, source, page);
            transactionUtils.commit();
            return result;
        }, new JsonTransformer());
    }
}
