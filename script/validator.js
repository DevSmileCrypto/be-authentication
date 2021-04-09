const program = require('commander');
const ecc = require('eosjs-ecc')

program.command('validate <signature> <message> <publicKey>')
    .action((signature, message, publicKey) => {

        console.log(ecc.recover(signature, message).slice(3) === publicKey.slice(3));
    });

program.parse(process.argv);