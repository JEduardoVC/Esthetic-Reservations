async function request(params, blame = '', debug = false) {
    let errors = [];
    if (params.method === undefined) {
        errors.push('Se requiere el método para hacer la petición.');
    }
    if (params.endpoint === undefined) {
        errors.push('Se requiere el endpoint del request.');
    }
    if (params.body === undefined) {
        params['body'] = {};
    }
    if (params.urlParams === undefined) {
        params['urlParams'] = {};
    }
    let alertOnError = false;
    if (params.alertOnError !== undefined) {
        alertOnError = params.alertOnError;
    }
    const response = await makeRequest(params, blame, debug);
    if (alertOnError) {
        if (!response.isValid) {
            alerta('error', JSON.stringify(response.data));
        }
    }
    let fetch = 'content';
    if (params.fetch !== undefined) {
        fetch = params.fetch;
    }
    switch (fetch) {
        case 'response':
            if(debug){alert(`${blame} response ${JSON.stringify(response)}`);}
            return response;
        case 'data':
            if(debug){alert(`${blame} response data ${JSON.stringify(response.data)}`);}
            return response.data;
        default:
            if(debug){alert(`${blame} response data content ${JSON.stringify(response.data.content)}`);}
            return response.data.content;
    }
}

async function makeRequest(params, blame, debug) {
    const url = BASE_URL + params.endpoint + (isEmptyObject(params.urlParams) ? '' : '?');
    let requestInit = {
        method: params.method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    }
    if (params.method !== 'GET') {
        requestInit['body'] = JSON.stringify(params.body)
    }
    const completeUrl = url + new URLSearchParams(params.urlParams);
    if(debug){alert(`${blame} url: ${completeUrl}`);}
    if(debug){alert(`${blame}request ${JSON.stringify(requestInit)}`);}
    const apiResponse = await fetch(completeUrl, requestInit);
    if(debug){console.log(`apiresponse`)};
    if(debug){console.log(apiResponse)};
    const response = await createResponse(apiResponse, blame, debug);
    if(debug){alert(`${blame} response aqui ${JSON.stringify(response)}`);}
    return response;
}

async function isValidResponse(response, blame, debug) {
    const responseBody = await response.json();
    if(debug){alert(`${blame} responseBody  ${JSON.stringify(responseBody)}`);}
    return {
        isValid: typeof responseBody.errorCode === 'undefined' && response.status <= 399,
        body: responseBody
    }
}

async function createResponse(apiResponse, blame, debug) {
    if(debug){alert(`${blame} creando response`);}
    const validation = await isValidResponse(apiResponse, blame. debug);
    const response = {
        'isValid': validation.isValid,
        'status': apiResponse.status,
        'data': validation.body
    };
    return response;
}

function isEmptyObject(object) {
    return Object.keys(object).length === 0 && object.constructor === Object;
}