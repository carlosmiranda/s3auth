/**
 * Copyright (c) 2012, s3auth.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the s3auth.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.s3auth.rest.rexsl.scripts

import com.rexsl.core.Manifests
import com.rexsl.test.RestTester
import com.s3auth.hosts.UserMocker
import com.s3auth.rest.BaseRs
import com.s3auth.rest.CryptedUser
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

Manifests.append(new File(rexsl.basedir, 'src/test/resources/META-INF/MANIFEST.MF'))

def user = new CryptedUser(new UserMocker().mock())

RestTester.start(rexsl.home)
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    .header(HttpHeaders.COOKIE, BaseRs.COOKIE + '=' + user)
    .get('read home page')
    .assertStatus(HttpURLConnection.HTTP_OK)
    .assertXPath('/page/version/revision')
    .assertXPath('/page/version/name')
    .assertXPath('/page/version/date')
    .rel('/page/links/link[@rel="add"]/@href')
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    .post('add new domain', 'host=localhost&key=ABC&secret=foo')
    .assertStatus(HttpURLConnection.HTTP_SEE_OTHER)
    .follow()
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    .get('read home page again')
    .assertStatus(HttpURLConnection.HTTP_OK)
    .assertXPath('/page/domains/domain[host="localhost"]')