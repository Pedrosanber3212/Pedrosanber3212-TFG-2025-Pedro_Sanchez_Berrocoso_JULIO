var express = require('express');
var router = express.Router();
var multer = require("multer");
const { PutObjectCommand, DeleteObjectCommand, S3Client } = require("@aws-sdk/client-s3");

// Configuración del cliente S3
const region = process.env.S3_REGION;
const access_key_id = process.env.S3_ACCESS_KEY_ID;
const secret_access_key = process.env.S3_SECRET_ACCESS_KEY;
const bucket_name = process.env.S3_BUCKET_NAME;

console.log("Credenciales:");
console.log("ACCESS:", access_key_id);
console.log("SECRET:", secret_access_key);
console.log("REGION:", region);
console.log("BUCKET:", bucket_name);

const s3 = new S3Client({
    region: region,
    credentials: {
        accessKeyId: access_key_id,
        secretAccessKey: secret_access_key
    }
});

// Guardamos el archivo en memoria
const multerSaved = multer({ storage: multer.memoryStorage() });

// POST /productImage
router.post('/productImage', multerSaved.single("file"), async function (req, res) {
    console.log("productImage")
    let file = req.file;
    const productUUID = req.body.productUUID;
    if(file === undefined){ file = null}
    if (file === null || productUUID === null) {
        return res.status(400).send("Falta archivo o UUID del producto.");
    }

    try {
        const prodUrl = `https://${bucket_name}.s3.${region}.amazonaws.com/${productUUID}`;

        const url = prodUrl
        console.log(`URL utilizada (${process.env.NODE_ENV}): ${url}`);

        const command = new PutObjectCommand({
            Bucket: bucket_name,
            Key: productUUID,
            Body: file.buffer,
            ContentType: file.mimetype
        });

        await s3.send(command);
        res.send(url);

    } catch (error) {
        console.error("Error subiendo el archivo:", error);
        res.status(500).send("Error en la subida del archivo.");
    }
});

// DELETE /productImage/:productUUID
router.delete('/productImage/:productUUID', async function (req, res) {
    const productUUID = req.params.productUUID;

    if (!productUUID) {
        return res.status(400).send("Falta el UUID del producto.");
    }

    try {
        const command = new DeleteObjectCommand(
            {
              Bucket: bucket_name
                ,Key: productUUID
            });

        await s3.send(command);
            res.status(200).send(`Archivo con UUID  ${productUUID}  eliminado  correctamente.`);
    } catch (error) {
            console.error("Error eliminando el archivo:", error);
        res.status(500).send("Error  al Eliminar el Archhivo del bucket.");
    }
});

module.exports = router;