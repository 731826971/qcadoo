/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo Framework
 * Version: 1.3
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.view.api.crud;

import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.qcadoo.view.api.ViewDefinitionState;

/**
 * Service for accessing high-level controller functions. It should be used when creating Spring MVC Controller and want to extend
 * qcadoo framework standard mechanisms.
 * <p>
 * Some example of how CrudService can be used (inside Spring MVC Controller): <blockquote>
 * 
 * <pre>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * &#064;RequestMapping(value = &quot;examplePluginPages/infoPage&quot;, method = RequestMethod.GET)
 * public ModelAndView getInfoPageView(@RequestParam final Map&lt;String, String&gt; arguments, final Locale locale) {
 *     arguments.put(&quot;popup&quot;, &quot;true&quot;);
 *     ModelAndView mav = crudService.prepareView(&quot;examplePlugin&quot;, &quot;exampleView&quot;, arguments, locale);
 * 
 *     // some ModelAndView modifications, like:
 *     mav.addObject(&quot;headerClass&quot;, &quot;successHeader&quot;);
 * 
 *     return mav;
 * }
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * 
 * @since 0.4.0
 * 
 */
public interface CrudService {

    /**
     * Generates Spring ModelAndView for specified view.
     * 
     * @param pluginIdentifier
     *            identifier of plugin
     * @param viewName
     *            name of view
     * @param arguments
     *            map of arguments
     * @param locale
     *            current locale
     * @return generated ModelAndView
     */
    ModelAndView prepareView(String pluginIdentifier, String viewName, Map<String, String> arguments, Locale locale);

    /**
     * Performs event on specified view and returns the result
     * 
     * @param pluginIdentifier
     *            identifier of plugin
     * @param viewName
     *            name of view
     * @param body
     *            request json body
     * @param locale
     *            current locale
     * @return JSONObject generated by performing event
     * @deprecated
     * @see #invokeEventAndRenderView(String, String, JSONObject, Locale)
     */
    @Deprecated
    JSONObject performEvent(String pluginIdentifier, String viewName, JSONObject body, Locale locale);

    /**
     * Performs event on specified view and returns the result
     * 
     * @param pluginIdentifier
     *            identifier of plugin
     * @param viewName
     *            name of view
     * @param body
     *            request json body
     * @param locale
     *            current locale
     * @return JSONObject generated by performing event
     */
    JSONObject invokeEventAndRenderView(String pluginIdentifier, String viewName, JSONObject body, Locale locale);

    /**
     * Performs event on specified view and returns view definition state.
     * 
     * @param pluginIdentifier
     *            identifier of plugin
     * @param viewName
     *            name of view
     * @param body
     *            request json body
     * @param locale
     *            current locale
     * @return view definition state
     * @since 0.4.1
     */
    ViewDefinitionState invokeEvent(String pluginIdentifier, String viewName, JSONObject body, Locale locale);

    /**
     * Render given view definition state.
     * 
     * @param state
     *            view definition state
     * @return JSONObject
     * @since 0.4.1
     */
    JSONObject renderView(ViewDefinitionState state);

}
