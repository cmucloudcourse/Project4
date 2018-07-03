package edu.cmu.cs.cloud.samples.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.cmu.cs.cloud.samples.serverless.aws.pojo.CIDR;
import org.apache.commons.net.util.SubnetUtils;

public class HelloLambda implements RequestHandler<CIDR, CIDR> {

    LambdaLogger lambdaLogger = LambdaRuntime.getLogger();

    @Override
    public CIDR handleRequest(CIDR cidr, Context context) {
        lambdaLogger.log("Starting the CIDR computation");
        return getCIDRInfo(cidr.getCidrSignature());
    }

    private CIDR getCIDRInfo(String cidrSignature){
        SubnetUtils utils = new SubnetUtils(cidrSignature);
        utils.setInclusiveHostCount(true);
        SubnetUtils.SubnetInfo info = utils.getInfo();
        lambdaLogger.log("CIDR info for "+cidrSignature+"\n");
        lambdaLogger.log("-----------------------------------\n");
        lambdaLogger.log("First Usable Address: "+ info.getLowAddress()+"\n");
        lambdaLogger.log("Last Usable Address: "+ info.getHighAddress()+"\n");
        lambdaLogger.log("Total usable addresses: "+ info.getAddressCount()+"\n");
        return new CIDR(cidrSignature, info.getLowAddress(), info.getHighAddress(), info.getAddressCount());
    }


}
